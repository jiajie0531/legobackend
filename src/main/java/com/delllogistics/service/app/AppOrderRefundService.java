package com.delllogistics.service.app;

import com.delllogistics.entity.enums.*;
import com.delllogistics.entity.logistics.LogisticsExpress;
import com.delllogistics.entity.order.*;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.logistics.LogisticsExpressRepository;
import com.delllogistics.repository.order.OrderItemRepository;
import com.delllogistics.repository.logistics.DeliveryAddressRepository;
import com.delllogistics.repository.order.OrderMainRepository;
import com.delllogistics.repository.order.OrderRefundLogisticsRepository;
import com.delllogistics.repository.order.OrderRefundRepository;
import com.delllogistics.repository.sys.SysAreaRepository;
import com.delllogistics.sequence.Sequence;
import com.delllogistics.service.order.OrderActionLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 退还订单
 * Created by calvin  2018/3/5
 */
@Service
public class AppOrderRefundService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final LogisticsExpressRepository logisticsExpressRepository;
    private final OrderActionLogService orderActionLogService;
    private final OrderItemRepository orderItemRepository;
    private final OrderMainRepository orderMainRepository;
    private final OrderRefundRepository orderRefundRepository;
    private final OrderRefundLogisticsRepository orderRefundLogisticsRepository;
    private final Sequence sequence;
    private final SysAreaRepository sysAreaRepository;

    public AppOrderRefundService(DeliveryAddressRepository deliveryAddressRepository, LogisticsExpressRepository logisticsExpressRepository, OrderActionLogService orderActionLogService, OrderItemRepository orderItemRepository, OrderMainRepository orderMainRepository, OrderRefundRepository orderRefundRepository, OrderRefundLogisticsRepository orderRefundLogisticsRepository, Sequence sequence, SysAreaRepository sysAreaRepository) {
        this.deliveryAddressRepository = deliveryAddressRepository;
        this.logisticsExpressRepository = logisticsExpressRepository;
        this.orderActionLogService = orderActionLogService;
        this.orderItemRepository = orderItemRepository;
        this.orderMainRepository = orderMainRepository;
        this.orderRefundRepository = orderRefundRepository;
        this.orderRefundLogisticsRepository = orderRefundLogisticsRepository;
        this.sequence = sequence;
        this.sysAreaRepository = sysAreaRepository;
    }


    public BigDecimal findSumRefundAmount(User user) {
        Long userId = user.getId();
        String sumRefundAmount = orderRefundRepository.getSumRefundAmount(userId);
        return new BigDecimal(sumRefundAmount);
    }

    public Page<OrderItem> findAll(int page, int size, OrderRefundStatus orderRefundStatus, User user) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<OrderItem> specification = (Root<OrderItem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> joinOrderMain = root.join("orderMain");// 注意join这里不用这个会关联多个
            if (orderRefundStatus != null) {
                //锁定已在租，待退租
                if (orderRefundStatus.equals(OrderRefundStatus.RENT) || orderRefundStatus.equals(OrderRefundStatus.WAIT_SURRENDER)) {
                    predicates.add(criteriaBuilder.equal(joinOrderMain.get("status"), OrderMainStatus.DELIVERY));//交付状态
                    predicates.add(criteriaBuilder.isNull(root.get("orderRefundStatus")));//没有进行退租状态
                    predicates.add(criteriaBuilder.isNotNull(root.get("expiryTime")));//已送达订单
                    //已在租=在租赁日期范围内
                    if (orderRefundStatus.equals(OrderRefundStatus.RENT)) {
                        predicates.add(criteriaBuilder.lessThan(
                                criteriaBuilder.function("now", Date.class), root.get("expiryTime")
                        ));//还在租赁日期内
                    }
                    //待退还 = 租赁日期已过需要退还
                    if (orderRefundStatus.equals(OrderRefundStatus.WAIT_SURRENDER)) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                                criteriaBuilder.function("now", Date.class), root.get("expiryTime")
                        ));//租赁日期到期或过期
                    }
                } else {
                    //待收货
                    if (orderRefundStatus.equals(OrderRefundStatus.WAIT_SELLER_AGREE)) {
                        predicates.add(criteriaBuilder.equal(joinOrderMain.get("status"), OrderMainStatus.DELIVERY));//交付状态
                        predicates.add(criteriaBuilder.equal(root.get("orderRefundStatus"), OrderRefundStatus.WAIT_SELLER_AGREE));//申请退租待确认
                    }
                    //待确认
                    if (orderRefundStatus.equals(OrderRefundStatus.WAIT_BUYER_CONFIRM)) {
                        predicates.add(criteriaBuilder.equal(joinOrderMain.get("status"), OrderMainStatus.DELIVERY));//退租中
                        predicates.add(criteriaBuilder.equal(root.get("orderRefundStatus"), OrderRefundStatus.WAIT_BUYER_CONFIRM));//待退货
                    }
                    //待退款
                    if (orderRefundStatus.equals(OrderRefundStatus.WAIT_SELLER_CONFIRM)) {
                        predicates.add(criteriaBuilder.equal(joinOrderMain.get("status"), OrderMainStatus.DELIVERY));//退租中
                        predicates.add(criteriaBuilder.equal(root.get("orderRefundStatus"), OrderRefundStatus.WAIT_SELLER_CONFIRM));//待退货
                    }
                    //退租中
                    if (orderRefundStatus.equals(OrderRefundStatus.REFUNDING)) {
                        predicates.add(criteriaBuilder.equal(joinOrderMain.get("status"), OrderMainStatus.DELIVERY));//退租中
                        predicates.add(criteriaBuilder.equal(root.get("orderRefundStatus"), OrderRefundStatus.REFUNDING));//退租中
                    }

                    //已退租 = 退款完毕
                    if (orderRefundStatus.equals(OrderRefundStatus.SUCCESS)) {
                        predicates.add(criteriaBuilder.equal(joinOrderMain.get("status"), OrderMainStatus.FINISHED));//订单完成
                        predicates.add(criteriaBuilder.equal(root.get("orderRefundStatus"), OrderRefundStatus.SUCCESS));//申请退租待确认
                    }
                }
            }
            predicates.add(criteriaBuilder.equal(joinOrderMain.get("isDeleted"), 0));
            predicates.add(criteriaBuilder.equal(joinOrderMain.get("user"), user.getId()));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return orderItemRepository.findAll(specification, pageable);
    }

    public  OrderRefund findOrderRefund(Long orderItemId ){
            return orderRefundRepository.findByOrderItem_Id(orderItemId);
    }

    @Transactional
    public void userConfrimRefundAmount(OrderRefund orderRefund, User user) {

        OrderItem orderItem = null;
        if (!StringUtils.isEmpty(orderRefund.getOrderItem()) && !StringUtils.isEmpty(orderRefund.getOrderItem().getId())) {
            orderItem = orderItemRepository.findOne(orderRefund.getOrderItem().getId());
        }
        if (orderItem == null) {
            throw new SystemException(ExceptionCode.INVALID_CANCEL_ORDER, "无效的取消订单请求!");
        }

        OrderRefund orderRefundNew = orderRefundRepository.findByOrderRefundStatusAndOrderItem_Id(orderItem.getOrderRefundStatus(),orderItem.getId());

        if (StringUtils.isEmpty(orderRefundNew)) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
        }
        OrderRefundStatus status = orderRefundNew.getOrderRefundStatus();
        switch (status) {
            case WAIT_BUYER_CONFIRM:
                OrderRefundStatus nextStatus = OrderRefundStatus.WAIT_SELLER_CONFIRM;
                orderItem.setOrderRefundStatus(nextStatus);
                orderRefundNew.setOrderItem(orderItem);
                orderRefundNew.setUpdateUser(user);
                orderRefundNew.setOrderRefundStatus(nextStatus);
                orderRefundRepository.save(orderRefundNew);
                orderActionLogService.saveOrderActionLog(orderRefundNew.getOrderItem().getOrderMain(), orderItem, OrderActionType.ORDER_REFUND, user, nextStatus.toString(),  nextStatus.description);
                //保存操作日志
                break;
            default:
                throw new SystemException(ExceptionCode.CANNOT_CANCEL_ORDER, "订单无法确认!");
        }


    }
    @Transactional
    public void apply(OrderRefund orderRefund, User user) {

        OrderItem orderItem = null;
        if (!StringUtils.isEmpty(orderRefund.getOrderItem()) && !StringUtils.isEmpty(orderRefund.getOrderItem().getId())) {
            orderItem = orderItemRepository.findOne(orderRefund.getOrderItem().getId());
        }
        if (orderItem == null) {
            throw new SystemException(ExceptionCode.INVALID_CANCEL_ORDER, "无效的取消订单请求!");
        }
        orderItem.setOrderRefundStatus(OrderRefundStatus.WAIT_SELLER_AGREE);
        orderRefund.setOrderItem(orderItem);//关联更新
        orderRefund.setOrderMain(orderItem.getOrderMain());
        orderRefund.setCompany(orderItem.getOrderMain().getCompany());
        orderRefund.setOrderRefundStatus(OrderRefundStatus.WAIT_SELLER_AGREE);
        orderRefund.setUser(user);
        orderRefund.setCode("OR" + String.valueOf(sequence.nextId()));//退款单编号
        //退货下只退款押金
        if(orderRefund.getOrderRefundType().equals(OrderRefundType.REFUND_GOODS)){
            orderRefund.setRefundAmount(orderItem.getOrderMain().getEnsureAmount());
        }else if(orderRefund.getOrderRefundType().equals(OrderRefundType.EXCHANGING_GOODS)){//换货情况退款金额是0
            orderRefund.setRefundAmount(BigDecimal.ZERO);
        }
        OrderRefundLogistics orderRefundLogistics = orderRefund.getOrderRefundLogistics();
        DeliveryAddress fetchAddress = orderRefundLogistics.getFetchAddress();//取件地址
        DeliveryAddress shippingAddress = orderRefundLogistics.getShippingAddress();//收件地址
        if (!StringUtils.isEmpty(fetchAddress) && !StringUtils.isEmpty(fetchAddress.getArea()) && !StringUtils.isEmpty(fetchAddress.getArea().getId())) {
            fetchAddress.setArea(sysAreaRepository.findOne(fetchAddress.getArea().getId()));
            fetchAddress.setUser(user);
        } else {
            fetchAddress = null;
        }
        if (!StringUtils.isEmpty(shippingAddress) && !StringUtils.isEmpty(shippingAddress.getArea()) && !StringUtils.isEmpty(shippingAddress.getArea().getId())) {
            shippingAddress.setArea(sysAreaRepository.findOne(shippingAddress.getArea().getId()));
            shippingAddress.setUser(user);
        } else {
            shippingAddress = null;
        }
        //如果是师傅上门 收件地址和取件地址
        if (orderRefundLogistics.getType().equals(OrderRefundLogisticsType.EMPLOYEE)) {
            if (fetchAddress != null) {
                orderRefundLogistics.setShippingAddress(shippingAddress);
            } else {
                throw new SystemException(ExceptionCode.INVALID_CANCEL_ORDER, "无效的参数!");
            }
            if (shippingAddress != null) {
                orderRefundLogistics.setFetchAddress(fetchAddress);
            }
        }

        //快递发货 只填写收件地址
        if (orderRefundLogistics.getType().equals(OrderRefundLogisticsType.EXPRESS)) {
            if (orderRefundLogistics.getLogisticsExpress() == null || orderRefundLogistics.getLogisticsExpress().getId() == null) {
                throw new SystemException(ExceptionCode.INVALID_CANCEL_ORDER, "无效的参数!");
            }
            LogisticsExpress logisticsCompany = logisticsExpressRepository.findOne(orderRefundLogistics.getLogisticsExpress().getId());
            if (logisticsCompany == null) {
                throw new SystemException(ExceptionCode.INVALID_CANCEL_ORDER, "无效的参数!");
            }
            orderRefundLogistics.setLogisticsExpress(logisticsCompany);
            if (shippingAddress != null) {
                orderRefundLogistics.setFetchAddress(shippingAddress);
            } else {
                throw new SystemException(ExceptionCode.INVALID_CANCEL_ORDER, "无效的参数!");
            }
        } else {
            orderRefundLogistics.setLogisticsExpress(null);
        }

        orderRefundLogistics.setUser(user);
        orderRefundLogistics.setCreateUser(user);
        orderItem.setOrderRefundStatus(OrderRefundStatus.WAIT_SELLER_AGREE);
        orderItem.setUpdateUser(user);
        orderItemRepository.save(orderItem);

        //退款单绑定退货物流->修改
        orderRefund.setOrderRefundLogistics(orderRefundLogistics);
        orderRefund.setOrderRefundStatus(OrderRefundStatus.WAIT_SELLER_AGREE);// 买家已经退货，等待卖家确认收货
        orderRefund.setUpdateUser(user);
        orderRefundRepository.save(orderRefund);

        //保存操作日志
        orderActionLogService.saveOrderActionLog(orderItem.getOrderMain(),orderItem, OrderActionType.ORDER_REFUND, user,  OrderRefundStatus.WAIT_SELLER_AGREE.toString(), "买家申请" + orderRefund.getOrderRefundType().description);



    }


}
