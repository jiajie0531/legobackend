package com.delllogistics.service.app;

import com.delllogistics.config.properties.WxPayProperties;
import com.delllogistics.dto.app.OrderCount;
import com.delllogistics.entity.Finance.FinanceReceivable;
import com.delllogistics.entity.app.ShoppingCart;
import com.delllogistics.entity.enums.*;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsDetail;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.user.MembershipRank;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.user.UserAccount;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.app.ShoppingCartRepository;
import com.delllogistics.repository.goods.GoodsDetailRepository;
import com.delllogistics.repository.goods.GoodsRepository;
import com.delllogistics.repository.order.OrderItemRepository;
import com.delllogistics.repository.order.OrderMainRepository;
import com.delllogistics.repository.sys.SysAreaRepository;
import com.delllogistics.repository.user.UserAccountRepository;
import com.delllogistics.repository.user.UserRepository;
import com.delllogistics.sequence.Sequence;
import com.delllogistics.service.InventoryOperationLogService;
import com.delllogistics.service.finance.FinanceReceivableService;
import com.delllogistics.service.order.OrderActionLogService;
import com.delllogistics.service.order.OrderTaskService;
import com.delllogistics.task.OrderTimeOutTask;
import com.delllogistics.util.*;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 订单
 * Created by xzm on 2017-12-13.
 */
@Service
public class AppOrderService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final InventoryOperationLogService inventoryOperationLogService;

    private final OrderMainRepository orderMainRepository;

    private final OrderItemRepository orderItemRepository;

    private final FinanceReceivableService financeReceivableService;

    private final GoodsRepository goodsRepository;

    private final GoodsDetailRepository goodsDetailRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    private final SysAreaRepository sysAreaRepository;

    private final Sequence sequence;

    private final OrderActionLogService orderActionLogService;

    private final WxPayService wxPayService;

    private final WxPayProperties wxPayProperties;

    private final UserRepository userRepository;

    private final ScheduledExecutorService scheduledExecutorService;

    private final OrderTaskService orderTaskService;

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public AppOrderService(OrderMainRepository orderMainRepository, OrderItemRepository orderItemRepository, FinanceReceivableService financeReceivableService, GoodsRepository goodsRepository, GoodsDetailRepository goodsDetailRepository, ShoppingCartRepository shoppingCartRepository, SysAreaRepository sysAreaRepository, Sequence sequence, OrderActionLogService orderActionLogService, WxPayService wxPayService, WxPayProperties wxPayProperties, UserRepository userRepository, ScheduledExecutorService scheduledExecutorService, OrderTaskService orderTaskService, InventoryOperationLogService inventoryOperationLogService, UserAccountRepository userAccountRepository) {
        this.orderMainRepository = orderMainRepository;
        this.orderItemRepository = orderItemRepository;
        this.financeReceivableService = financeReceivableService;
        this.goodsRepository = goodsRepository;
        this.goodsDetailRepository = goodsDetailRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.sysAreaRepository = sysAreaRepository;
        this.sequence = sequence;
        this.orderActionLogService = orderActionLogService;
        this.wxPayService = wxPayService;
        this.wxPayProperties = wxPayProperties;
        this.userRepository = userRepository;
        this.scheduledExecutorService = scheduledExecutorService;
        this.orderTaskService = orderTaskService;
        this.inventoryOperationLogService = inventoryOperationLogService;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public Long submitOrder(OrderMain orderMain, User user) {
        SysArea area = orderMain.getArea();
        if (area == null || area.getId() == null) {
            throw new SystemException(ExceptionCode.SYSAREA_ERROR, "订单配送地址无效");
        }
        SysArea sysArea = sysAreaRepository.findOne(area.getId());
        if (sysArea == null) {
            throw new SystemException(ExceptionCode.SYSAREA_ERROR, "订单配送地址无效");
        }
        orderMain.setArea(sysArea);

        user = userRepository.findOne(user.getId());
        if (user == null) {
            throw new GeneralException(BizExceptionEnum.USER_IS_NONE);
        }
        Company company = orderMain.getCompany();
        Company company1 = user.getCompany();

        if(company==null || company.getId()==null || !company.getId() .equals(company1.getId())){
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_COMPANY);
        }
        orderMain.setCompany(company1);
        UserAccount userAccount = userAccountRepository.findByUser_idAndCompany_idAndIsDeleted(user.getId(), company.getId(), false);
        if(userAccount==null){
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR_ACCOUNT);
        }
        BigDecimal rentalAmount = orderMain.getRentalAmount();
        BigDecimal orderTotalAmount = orderMain.getEnsureAmount().add(orderMain.getRentalAmount()).add(orderMain.getDeliveryFee());
        BigDecimal disAmount = BigDecimal.ZERO;
        MembershipRank membershipRank = userAccount.getMembershipRank();
        if (membershipRank != null && membershipRank.getDiscount() != null && !membershipRank.getDiscount().equals(BigDecimal.ZERO)) {
            BigDecimal subtract = BigDecimal.ONE.subtract(membershipRank.getDiscount());
            /*
                会员折扣率不能大于1
             */
            if (subtract.compareTo(BigDecimal.ONE) == 1) {
                throw new GeneralException(BizExceptionEnum.MEMBER_DIS_ERROR);
            }
            /*
                 验证会员折扣金额
             */
            disAmount = rentalAmount.multiply(subtract);
            disAmount = disAmount.setScale(2, BigDecimal.ROUND_HALF_UP);



            if (orderMain.getDiscountAmount().compareTo(disAmount) != 0) {
                throw new GeneralException(BizExceptionEnum.MEMBER_DIS_AMOUNT_ERROR);
            }
        }
        if (orderMain.getOrderAmount().compareTo(orderTotalAmount.subtract(disAmount)) != 0) {
            throw new GeneralException(BizExceptionEnum.PAY_AMOUNT_ERROR);
        }
        //保存订单明细
        List<OrderItem> orderItems = orderMain.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            Goods goods = goodsRepository.findOne(orderItem.getGoods().getId());
            orderItem.setGoods(goods);
            GoodsDetail goodsDetail = goodsDetailRepository.findOne(orderItem.getGoodsDetail().getId());
            orderItem.setGoodsDetail(goodsDetail);
            orderItem.setRentalPrice(goodsDetail.getRentalPrice());
            orderItem.setEnsurePrice(goodsDetail.getEnsurePrice());
            orderItemRepository.save(orderItem);
            if (orderMain.getCompany() == null) {
                orderMain.setCompany(goods.getCompany());
            }
                /*
                减少对应库存
                 */
            inventoryOperationLogService.recordGoodsStockLog(goodsDetail, orderItem.getQuantity(), InventoryOperate.SALES, user);

            //销量操作
            BigDecimal salesVolume = goodsDetail.getSalesVolume() == null ? BigDecimal.ZERO : goodsDetail.getSalesVolume();
            goodsDetail.setSalesVolume(salesVolume.add(orderItem.getQuantity()));
            goodsDetailRepository.save(goodsDetail);

            ShoppingCart shoppingCart = shoppingCartRepository.findByUserAndGoodsAndIsDeleted(user, goods, false);
            if (shoppingCart != null) {
                shoppingCartRepository.delete(shoppingCart);
            }
        }
        //保存提交订单
        orderMain.setUser(user);
        orderMain.setStatus(OrderMainStatus.WAIT_TO_PAY);
        orderMain.setCode("OD" + String.valueOf(sequence.nextId()));//订单编号
        orderMainRepository.save(orderMain);
        //保存操作日志
        orderActionLogService.saveOrderActionLog(orderMain, null, OrderActionType.ORDER_MAIN, user, OrderMainStatus.WAIT_TO_PAY.toString(), "等待买家付款");

        OrderTimeOutTask orderTimeOutTask = new OrderTimeOutTask(orderTaskService, orderMain.getId());
        scheduledExecutorService.schedule(orderTimeOutTask, wxPayProperties.getValidTime(), TimeUnit.MINUTES);

        return orderMain.getId() == null ? 0 : orderMain.getId();
    }


    public OrderCount countOrderuantity(User user, Long companyId) {
        int waitToPayCount = orderMainRepository.countAllByUserAndCompany_idAndStatusAndIsDeleted(user,companyId, OrderMainStatus.WAIT_TO_PAY, false);
        int paidCount = orderMainRepository.countAllByUserAndCompany_idAndStatusAndIsDeleted(user,companyId, OrderMainStatus.PAID, false);
        int waitToDeliveryCount = orderMainRepository.countAllByUserAndCompany_idAndStatusAndIsDeleted(user,companyId, OrderMainStatus.WAIT_TO_DELIVERY, false);
        //int deliveryCount = orderCountByStatus(OrderMainStatus.DELIVERY,user).size();
        int deliveryCount = orderMainRepository.countByUserAndStatusAndOrderItems(
                OrderMainStatus.DELIVERY.toString(),
                OrderItemEvaluateStatus.SHARE_FINISHED.toString(),
                user.getId(),companyId);

        OrderCount orderCount = new OrderCount();
        orderCount.setCountWaitToPay(waitToPayCount);
        orderCount.setCountPaid(paidCount);
        orderCount.setCountWaitToDelivery(waitToDeliveryCount);
        orderCount.setCountDelivery(deliveryCount);
        return orderCount;
    }


    public Page<OrderMain> findOrders(int page, int size, OrderMainStatus orderMainStatus, User user,Long companyId) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Specification<OrderMain> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> joinOrderItems = root.join("orderItems");// 注意join这里不用这个会关联多个
            if (orderMainStatus != null) {
                if (orderMainStatus.equals(OrderMainStatus.DELIVERY)) {
                    predicates.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), OrderMainStatus.DELIVERY),criteriaBuilder.equal(root.get("status"), OrderMainStatus.FINISHED)));
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.isNull(joinOrderItems.get("orderItemEvaluateStatus")),
                            criteriaBuilder.notEqual(joinOrderItems.get("orderItemEvaluateStatus"), OrderItemEvaluateStatus.SHARE_FINISHED)
                    ));
                } else {
                    predicates.add(criteriaBuilder.equal(root.get("status"), orderMainStatus));
                }
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));

            predicates.add(criteriaBuilder.equal(root.join("user"), user.getId()));
            predicates.add(criteriaBuilder.equal(root.join("company"), companyId));

            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return orderMainRepository.findAll(specification, pageable);
    }


    public OrderMain findOrder(long orderId) {
        OrderMain orderMain = orderMainRepository.findByIdAndIsDeleted(orderId, false);
        return orderMain;
    }

    public OrderMain findOrderMainByItem(long orderItemId) {
        OrderItem orderItem = orderItemRepository.findOne(orderItemId);
        return orderItem.getOrderMain();
    }

    public OrderItem findOrderItem(long orderItemId) {
        return orderItemRepository.findOne(orderItemId);
    }

    @Transactional
    public void cancelOrder(long orderId, User user) {
        OrderMain orderMain = orderMainRepository.findOne(orderId);
        if (!orderMain.getUser().getId().equals(user.getId())) {
            throw new SystemException(ExceptionCode.INVALID_CANCEL_ORDER, "无效的取消订单请求!");
        }
        OrderMainStatus status = orderMain.getStatus();
        switch (status) {
            case WAIT_TO_PAY:
                /*
                未支付的直接取消订单
                 */
                orderMain.setStatus(OrderMainStatus.CANCELED);
                //保存操作日志
                orderActionLogService.saveOrderActionLog(orderMain, null, OrderActionType.ORDER_MAIN, user, OrderMainStatus.CANCELED.toString(), "买家取消订单");
                break;
            case PAID:
                /*
                已支付的取消订单，需退回支付费用
                 */
                //保存操作日志
                orderActionLogService.saveOrderActionLog(orderMain, null, OrderActionType.ORDER_MAIN, user, OrderMainStatus.CANCELED.toString(), "买家取消订单");
                orderMain.setStatus(OrderMainStatus.CANCELED);
                //todo 发起退款请求
                break;
            default:
                throw new SystemException(ExceptionCode.CANNOT_CANCEL_ORDER, "订单无法取消!");
        }
        orderMainRepository.save(orderMain);

        List<OrderItem> orderItems = orderMain.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            inventoryOperationLogService.recordGoodsStockLog(orderItem.getGoodsDetail(), orderItem.getQuantity(), InventoryOperate.CANCEL_ORDER, user);
        }
    }


    @SuppressWarnings("Duplicates")
    @Transactional
    public WxPayMpOrderResult payOrder(long orderId, User user, HttpServletRequest request) {
        OrderMain orderMain = orderMainRepository.findOne(orderId);
        if (orderMain == null || !orderMain.getUser().getId().equals(user.getId())) {
            throw new SystemException(ExceptionCode.INVALID_PAY_ORDER, "无效的支付订单请求!");
        }
        OrderMainStatus status = orderMain.getStatus();
        BigDecimal orderAmount = orderMain.getRentalAmount().add(orderMain.getDeliveryFee()).add(orderMain.getEnsureAmount());

//            BigDecimal usableAmount = BigDecimal.ZERO;
//            BigDecimal frozenAmount = BigDecimal.ZERO;
        if (status == OrderMainStatus.WAIT_TO_PAY) {
            //未支付订单--支付
            //预存款支付逻辑暂时注释掉
//                orderMain.setStatus(OrderMainStatus.PAID);
//                FinanceDeposit financeDeposit = financeDepositService.findByUser_Id(user.getId());
//                if(!StringUtils.isEmpty(financeDeposit)
//                   && financeDeposit.getUsableAmount()!=null
//                ){
//                    usableAmount = financeDeposit.getUsableAmount();
//                }
//
//                if(!StringUtils.isEmpty(financeDeposit)
//                    && financeDeposit.getFrozenAmount()!=null
//                ){
//                    frozenAmount = financeDeposit.getFrozenAmount();
//                }
//
//                if((!Objects.equals(usableAmount, BigDecimal.ZERO)) && (usableAmount.compareTo(orderAmount) >= 0)){
//                    //这里预存款消费记录
//                    FinanceDepositLog financeDepositLog = new FinanceDepositLog();
//                    financeDepositLog.setDescription("支付成功");
//                    financeDepositLog.setPayChannel(PayChannel.WECHAT);
//                    financeDepositLog.setAmount(orderAmount);
//                    //当前用户预存款余额=可用金额+冻结金额
//                    financeDepositLog.setBalance(usableAmount.add(frozenAmount));
//                    financeDepositLog.setOrderMain(orderMain);
//                    financeDepositLogService.save(financeDepositLog);
//                }
            user = userRepository.findOne(user.getId());
            if (user.getWechatUser() == null) {
                throw new SystemException(ExceptionCode.NOT_WECHAT_USER, "非微信用户!");
            }
            //保存操作日志
//          orderActionLogService.saveOrderActionLog(orderMain, OrderActionType.ORDER_MAIN, user, OrderMainStatus.WAIT_WECHAT_PAY.toString(), "发起微信支付");

            /*
            交易有效支付时间
             */
            Date outTime = DateUtils.addDateByMinute(orderMain.getCreateTime(), wxPayProperties.getValidTime());

            /*
            验证订单有效支付时间
             */
            if (new Date().after(outTime)) {
                throw new GeneralException(BizExceptionEnum.ORDER_OUT_PAY_TIME);
            }

            orderAmount = PayUtil.setOrderAmount(wxPayProperties, orderAmount, orderMain.getOrderItems().size());
            logger.info("支付金额:{}", orderAmount);
            //财务收款单
            FinanceReceivable financeReceivable = new FinanceReceivable();
            financeReceivable.setOrderMain(orderMain);
            financeReceivable.setCode("FRV" + String.valueOf(sequence.nextId()));//收款单编号
            financeReceivable.setWaitingPayAmount(orderAmount);
            financeReceivable.setPayChannel(PayChannel.WECHAT);
            financeReceivable.setPayStatus(PayStatus.WAITING);
            financeReceivable.setPayType(PayType.ORDERBUY);
            financeReceivable.setUser(user);
            financeReceivable.setCreateUser(user);
            financeReceivable.setCompany(user.getCompany());


            /*
            本地开发环境，不发起微信支付请求
             */
            if (!wxPayProperties.isUseDevEnv()) {
                financeReceivableService.save(financeReceivable);
                /*
                测试支付金额为1分钱
                */
                String notifyUrl = wxPayProperties.getPayNotifyUrl();
                logger.info("支付通知Url:{}", notifyUrl);
                WxPayUnifiedOrderRequest prepayInfo = WxPayUnifiedOrderRequest.newBuilder().
                        openid(user.getWechatUser().getOpenId()).
                        outTradeNo(financeReceivable.getId().toString())
                        .totalFee(MoneyUtils.yuanToFen(orderAmount))
                        .body("租赁支付")
                        .tradeType("JSAPI")
                        .timeStart(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"))
                        .timeExpire(DateUtils.formatDate(outTime, "yyyyMMddHHmmss"))
                        .spbillCreateIp(HttpUtils.getRemortIP(request))
                        .notifyUrl(notifyUrl)
                        .build();
                Object obj;
                try {
                    WechatUtil.setSandboxSignKey(wxPayProperties, wxPayService);
                    obj = wxPayService.createOrder(prepayInfo);
                } catch (WxPayException e) {
                    logger.error("微信支付异常,{}:{}", e.getReturnMsg(), e.getMessage());
                    throw new SystemException(ExceptionCode.INVALID_WECHAT_PAY_REQUEST, "无效的微信支付请求!");
                }
                logger.info("pay result:{}", obj);
                if (obj != null) {
                    return (WxPayMpOrderResult) obj;
                } else {
                    throw new SystemException(ExceptionCode.INVALID_WECHAT_PAY_REQUEST, "无效的微信支付请求!");
                }
            } else {
                /*
                本地开发环境支付时，直接保存为已支付成功，并记录支付日志
                 */
                financeReceivable.setPayStatus(PayStatus.SUCCESS);
                financeReceivable.setPayTime(new Date());
                financeReceivable.setTransactionCode("test");
                financeReceivable.setPayAmount(financeReceivable.getWaitingPayAmount());
                orderMain.setStatus(OrderMainStatus.PAID);
                financeReceivable.setCreateUser(orderMain.getUser());
                financeReceivableService.save(financeReceivable);
                //保存操作日志
                orderActionLogService.saveOrderActionLog(orderMain, null, OrderActionType.ORDER_MAIN, orderMain.getUser(), OrderMainStatus.PAID.toString(), "微信支付成功");
                return null;
            }

        } else {
            throw new SystemException(ExceptionCode.CANNOT_PAY_ORDER, "订单无法支付!");
        }

    }





    @Transactional
    public void applyRenew(long orderId, int buyNum, User user) {

        OrderMain orderMain = orderMainRepository.findOne(orderId);
        if (!orderMain.getUser().getId().equals(user.getId())) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "无效权限!");
        }
        if (orderMain.getStatus() != OrderMainStatus.DELIVERY) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "订单状态无效!");
        }
        orderMain.setDayNum(orderMain.getDayNum() + buyNum);//天数累加
        orderMainRepository.save(orderMain);


        //财务收款单
        FinanceReceivable financeReceivable = new FinanceReceivable();
        financeReceivable.setOrderMain(orderMain);
        financeReceivable.setCode("FRV" + String.valueOf(sequence.nextId()));//收款单编号
        financeReceivable.setPayAmount(orderMain.getRentalAmount().add(orderMain.getDeliveryFee()).add(orderMain.getEnsureAmount()));
        financeReceivable.setPayChannel(PayChannel.WECHAT);
        financeReceivable.setPayStatus(PayStatus.SUCCESS);
        financeReceivable.setPayType(PayType.RENEW);
        financeReceivable.setTransactionCode("12312333223");//微信支付凭证号
        financeReceivable.setUser(user);
        financeReceivable.setCreateUser(user);
        financeReceivableService.save(financeReceivable);


        //订单操作记录
        orderActionLogService.saveOrderActionLog(orderMain, null, OrderActionType.PAY, user, PayType.RENEW.toString(), "买家已续租");


    }


    @Transactional
    public String resultnotify(HttpServletRequest request) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            logger.info("支付返回xml：{}", xmlResult);
            WxPayOrderNotifyResult wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(xmlResult);
            logger.info("支付结果：{}", wxPayOrderNotifyResult);
            if (wxPayOrderNotifyResult != null && wxPayOrderNotifyResult.getResultCode().equals("SUCCESS")) {
                /*
                  支付成功
                 */
                String outTradeNo = wxPayOrderNotifyResult.getOutTradeNo();
                FinanceReceivable payLogInfo = financeReceivableService.findOne(Long.valueOf(outTradeNo));
                if (payLogInfo == null) {
                    logger.error("支付日志id:{}不存在", outTradeNo);
                    return WxPayNotifyResponse.success("支付ID不存在");
                }
                if (!payLogInfo.getPayStatus().equals(PayStatus.WAITING)) {
                    logger.error("支付日志ID:{},状态不是待支付", outTradeNo);
                    return WxPayNotifyResponse.success("支付状态异常");
                }
                OrderMain orderMain = payLogInfo.getOrderMain();
                if (!orderMain.getStatus().equals(OrderMainStatus.WAIT_TO_PAY)) {
                    logger.error("订单ID:{},状态不是待支付", orderMain.getId());
                    return WxPayNotifyResponse.success("支付状态异常");
                }
                Integer totalFeeInt = wxPayOrderNotifyResult.getTotalFee();
                BigDecimal totalFeeYuan = MoneyUtils.fenToYuan(totalFeeInt);
                if (!totalFeeYuan.equals(payLogInfo.getWaitingPayAmount())) {
                    logger.error("订单ID:{},支付金额异常", outTradeNo);
                    payLogInfo.setPayStatus(PayStatus.PAYAMOUNTWRONG);
                } else {
                    payLogInfo.setPayStatus(PayStatus.SUCCESS);
                }
                payLogInfo.setPayTime(new Date());
                payLogInfo.setTransactionCode(wxPayOrderNotifyResult.getTransactionId());
                payLogInfo.setPayAmount(totalFeeYuan);
                orderMain.setStatus(OrderMainStatus.PAID);
                payLogInfo.setCreateUser(orderMain.getUser());
                financeReceivableService.save(payLogInfo);
                //保存操作日志
                orderActionLogService.saveOrderActionLog(orderMain, null, OrderActionType.ORDER_MAIN, orderMain.getUser(), OrderMainStatus.PAID.toString(), "微信支付成功");
                return WxPayNotifyResponse.success("支付成功");
            } else {
                return WxPayNotifyResponse.success("支付失败");
            }
        } catch (Exception e) {
            logger.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }

    }

    public void checkTimeoutPayOrder() {
        List<OrderMain> orderMains = orderMainRepository.findAllByIsDeletedAndStatus(false, OrderMainStatus.WAIT_TO_PAY);
        logger.info("检查超时支付订单，数量:{}", orderMains.size());
        for (OrderMain orderMain : orderMains) {
            Long id = orderMain.getId();
            OrderTimeOutTask orderTimeOutTask = new OrderTimeOutTask(orderTaskService, id);
            Date now = new Date();
            /*
            计算订单过期时间
             */
            Date outTime = DateUtils.addDateByMinute(orderMain.getCreateTime(), wxPayProperties.getValidTime());
            logger.info("处理未支付订单，id：{},超时时间：{}", id, outTime);
            if (now.after(outTime)) {
                /*
                已超过允许支付时间,立即更改超时
                 */

                scheduledExecutorService.execute(orderTimeOutTask);
            } else {
                /*
                未超时订单，增加延期检查订单任务
                 */
                long outMilliseconds = outTime.getTime() - now.getTime();
                scheduledExecutorService.schedule(orderTimeOutTask, outMilliseconds, TimeUnit.MILLISECONDS);
            }

        }
    }



}

