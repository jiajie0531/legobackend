package com.delllogistics.service.order;

import com.delllogistics.dto.order.OrderMainSearch;
import com.delllogistics.entity.enums.OrderActionType;
import com.delllogistics.entity.enums.OrderLogisticsType;
import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.order.OrderMainRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class OrderMainService {


    private final OrderMainRepository orderMainRepository;
    private final OrderActionLogService orderActionLogService;



    public OrderMainService(OrderMainRepository orderMainRepository, OrderActionLogService orderActionLogService) {
        this.orderMainRepository = orderMainRepository;
        this.orderActionLogService = orderActionLogService;
    }


    public Page<OrderMain> findOrders( OrderMainSearch orderMainSearch ) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(orderMainSearch.getPage(), orderMainSearch.getSize(), sort);
        Specification<OrderMain> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (orderMainSearch.getStatus() != null  && orderMainSearch.getStatus()!= OrderMainStatus.ALL ) {
                predicates.add(criteriaBuilder.equal(root.get("status"), orderMainSearch.getStatus() ));
            }
            if ( !StringUtils.isEmpty(orderMainSearch.getCode())) {
                predicates.add(criteriaBuilder.equal(root.get("code"), orderMainSearch.getCode()));
            }
            if ( !StringUtils.isEmpty(orderMainSearch.getConsignee())) {
                predicates.add(criteriaBuilder.equal(root.get("consignee"), orderMainSearch.getConsignee()));
            }
            if (!StringUtils.isEmpty(orderMainSearch.getUsername()) ) {
                predicates.add(
                        criteriaBuilder.equal(root.join("user").get("username"), orderMainSearch.getUsername())
                );
            }

            if (!StringUtils.isEmpty(orderMainSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(root.get("company").get("id"), orderMainSearch.getCompanyId()));
            }
            if (!StringUtils.isEmpty(orderMainSearch.getPhone() )) {
                predicates.add(criteriaBuilder.equal(root.get("phone"), orderMainSearch.getPhone()));
            }
            if (!StringUtils.isEmpty(orderMainSearch.getStartTime()) && !StringUtils.isEmpty(orderMainSearch.getEndTime()) ) {
                predicates.add(
                        criteriaBuilder.between(root.get("createTime"), orderMainSearch.getStartTime(),orderMainSearch.getEndTime())
                );
            }
            if (orderMainSearch.getOrderLogisticsType() != null  && orderMainSearch.getOrderLogisticsType()!= OrderLogisticsType.ALL)  {
                predicates.add(criteriaBuilder.equal(root.get("orderLogisticsType"), orderMainSearch.getOrderLogisticsType()));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return orderMainRepository.findAll(specification, pageable);
    }
    public OrderMain findOne(Long orderMainId){
        return orderMainRepository.findOne(orderMainId);
    }

    public void cancelOrder(OrderMain orderMain, User user) {

        if (StringUtils.isEmpty(orderMain.getId()) || StringUtils.isEmpty(orderMain.getRemarks())) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
        }
        orderMain = orderMainRepository.findOne(orderMain.getId());
        OrderMainStatus status = orderMain.getStatus();
        switch (status) {
            case WAIT_TO_PAY:
                /*
                未支付的直接取消订单
                 */
                orderMain.setStatus(OrderMainStatus.CANCELED);
                orderActionLogService.saveOrderActionLog(orderMain,null, OrderActionType.ORDER_MAIN, user,  OrderMainStatus.CANCELED.toString(),"卖家已取消订单");
                //保存操作日志
                break;
            default:
                throw new SystemException(ExceptionCode.CANNOT_CANCEL_ORDER, "订单无法取消!");
        }
        orderMainRepository.save(orderMain);
    }




    public OrderMain findOrderByCode(String code) {
        Specification<OrderMain> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("code"), code));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return orderMainRepository.findOne(specification);
    }

    public void saveOrder(OrderMain order) {
        orderMainRepository.save(order);
    }




}
