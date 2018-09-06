package com.delllogistics.service.order;

import com.delllogistics.config.properties.WxPayProperties;
import com.delllogistics.dto.order.OrderRefundSearch;
import com.delllogistics.entity.Finance.FinanceReceivable;
import com.delllogistics.entity.Finance.FinanceRefund;
import com.delllogistics.entity.enums.*;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.order.OrderRefund;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.finance.FinanceReceivableRepository;
import com.delllogistics.repository.order.OrderItemRepository;
import com.delllogistics.repository.order.OrderRefundRepository;
import com.delllogistics.sequence.Sequence;
import com.delllogistics.service.InventoryOperationLogService;
import com.delllogistics.service.finance.FinanceRefundService;
import com.delllogistics.service.user.MembershipPointService;
import com.delllogistics.util.DateUtils;
import com.delllogistics.util.MoneyUtils;
import com.delllogistics.util.PayUtil;
import com.delllogistics.util.WechatUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class OrderRefundService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderActionLogService orderActionLogService;
    private final OrderItemRepository orderItemRepository;
    private final OrderRefundRepository orderRefundRepository;
    private final FinanceRefundService financeRefundService;
    private final FinanceReceivableRepository financeReceivableRepository;
    private final WxPayService wxPayService;
    private final WxPayProperties wxPayProperties;
    private final MembershipPointService membershipPointService;
    private final InventoryOperationLogService inventoryOperationLogService;

    private final Sequence sequence;

    public OrderRefundService(OrderActionLogService orderActionLogService, OrderItemRepository orderItemRepository, OrderRefundRepository orderRefundRepository, FinanceRefundService financeRefundService, FinanceReceivableRepository financeReceivableRepository, WxPayService wxPayService, WxPayProperties wxPayProperties, MembershipPointService membershipPointService, InventoryOperationLogService inventoryOperationLogService, Sequence sequence) {
        this.orderActionLogService = orderActionLogService;
        this.orderItemRepository = orderItemRepository;
        this.orderRefundRepository = orderRefundRepository;
        this.financeRefundService = financeRefundService;
        this.financeReceivableRepository = financeReceivableRepository;
        this.wxPayService = wxPayService;
        this.wxPayProperties = wxPayProperties;
        this.membershipPointService = membershipPointService;
        this.inventoryOperationLogService = inventoryOperationLogService;
        this.sequence = sequence;
    }


    public Page<OrderRefund> findAll(OrderRefundSearch orderRefundSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(orderRefundSearch.getPage(), orderRefundSearch.getSize(), sort);
        Specification<OrderRefund> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> joinOrderMain = root.join("orderMain");// 注意join这里不用这个会关联多个
            Join<Object, Object> joinUser = joinOrderMain.join("user");// 注意join这里不用这个会关联多个

            if (orderRefundSearch.getOrderRefundStatus() != null && (orderRefundSearch.getOrderRefundStatus() != OrderRefundStatus.ALL)) {
                predicates.add(criteriaBuilder.equal(root.get("orderRefundStatus"), orderRefundSearch.getOrderRefundStatus()));
            }
            if (!StringUtils.isEmpty(orderRefundSearch.getOrderRefundCode())) {
                predicates.add(criteriaBuilder.equal(root.get("code"), orderRefundSearch.getOrderRefundCode()));
            }
            if (!StringUtils.isEmpty(orderRefundSearch.getOrderMainCode())) {
                predicates.add(criteriaBuilder.equal(joinOrderMain.get("code"), orderRefundSearch.getOrderMainCode()));
            }
            if (!StringUtils.isEmpty(orderRefundSearch.getConsignee())) {
                predicates.add(criteriaBuilder.equal(joinOrderMain.get("consignee"), orderRefundSearch.getConsignee()));
            }
            if (!StringUtils.isEmpty(orderRefundSearch.getUsername())) {
                predicates.add(criteriaBuilder.equal(joinUser.get("username"), orderRefundSearch.getUsername()));
            }
            if (!StringUtils.isEmpty(orderRefundSearch.getPhone())) {
                predicates.add(criteriaBuilder.equal(joinOrderMain.get("phone"), orderRefundSearch.getPhone()));
            }
            if (!StringUtils.isEmpty(orderRefundSearch.getCompanyId())) {
                predicates.add(criteriaBuilder.equal(joinOrderMain.get("company").get("id"), orderRefundSearch.getCompanyId()));
            }
            if (!StringUtils.isEmpty(orderRefundSearch.getOrderRefundType()) && orderRefundSearch.getOrderRefundType() != OrderRefundType.ALL) {
                predicates.add(criteriaBuilder.equal(root.get("orderRefundType"), orderRefundSearch.getOrderRefundType()));
            }
            if (!StringUtils.isEmpty(orderRefundSearch.getStartTime()) && !StringUtils.isEmpty(orderRefundSearch.getEndTime())) {
                predicates.add(
                        criteriaBuilder.between(root.get("createTime"), orderRefundSearch.getStartTime(), orderRefundSearch.getEndTime())
                );
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            criteriaQuery.groupBy(root.get("id"));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return orderRefundRepository.findAll(specification, pageable);
    }

    public OrderRefund findOrderRefund(OrderRefundSearch orderRefundSearch) {
        return orderRefundRepository.findOne(orderRefundSearch.getOrderRefundId());
    }

    @Transactional
    public void agreeOrderRefund(OrderRefund orderRefund, User user) {
        if (StringUtils.isEmpty(orderRefund.getId())) {
            throw new GeneralException(BizExceptionEnum.CLASS_FORMAT_ERROR);
        }
        OrderRefund orderRefundNew = orderRefundRepository.findOne(orderRefund.getId());

        if (StringUtils.isEmpty(orderRefundNew)) {
            throw new GeneralException(BizExceptionEnum.CLASS_FORMAT_ERROR);
        }
        if (orderRefundNew.getOrderItem().getEnsurePrice().compareTo(orderRefund.getChargeAmount()) < 1) {
            throw new GeneralException(BizExceptionEnum.HARGEAMOUNT_BIG);
        }

        OrderRefundStatus status = orderRefundNew.getOrderRefundStatus();
        switch (status) {
            case WAIT_SELLER_AGREE:
                OrderRefundStatus nextStatus = OrderRefundStatus.WAIT_BUYER_CONFIRM;

                orderRefundNew.setChargeAmount(orderRefund.getChargeAmount());
                orderRefundNew.setChargeDescription(orderRefund.getChargeDescription());
                orderRefundNew.setChargeType(orderRefund.getChargeType());
                orderRefundNew.setIsCharge(orderRefund.getIsCharge());
                OrderItem orderItem = orderRefundNew.getOrderItem();
                orderItem.setOrderRefundStatus(nextStatus);
                orderRefundNew.setOrderItem(orderItem);
                orderRefundNew.setUpdateUser(user);
                orderRefundNew.setOrderRefundStatus(nextStatus);
                orderRefundRepository.save(orderRefundNew);
                orderActionLogService.saveOrderActionLog(orderRefundNew.getOrderItem().getOrderMain(), orderItem, OrderActionType.ORDER_REFUND, user, nextStatus.toString(), nextStatus.description);
                //保存操作日志
                break;
            default:
                throw new GeneralException(BizExceptionEnum.CLASS_FORMAT_ERROR);

        }


    }

    @Transactional
    public void sellerConfirm(OrderRefund orderRefund, User user) {

        if (StringUtils.isEmpty(orderRefund.getId())) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
        }
        orderRefund = orderRefundRepository.findOne(orderRefund.getId());

        if (StringUtils.isEmpty(orderRefund)) {
            throw new SystemException(ExceptionCode.INVALID_ERRO, "无效的参数!");
        }
        OrderRefundStatus status = orderRefund.getOrderRefundStatus();
        switch (status) {
            case ALL:
                break;
            case RENT:
                break;
            case WAIT_SURRENDER:
                break;
            case WAIT_SELLER_AGREE:
                break;
            case WAIT_BUYER_CONFIRM:
                break;
            case WAIT_SELLER_CONFIRM:

                OrderItem orderItem = orderRefund.getOrderItem();
                OrderMain orderMain = orderItem.getOrderMain();
                /*
                退款必须要有支付成功的收款单
                 */
                FinanceReceivable financeReceivable = financeReceivableRepository.findByOrderMain_IdAndPayStatus(orderMain.getId(), PayStatus.SUCCESS);
                if (financeReceivable == null) {
                    throw new GeneralException(BizExceptionEnum.NO_VALID_FINANCERECEIVABLE);
                }

                orderItem.setOrderRefundStatus(OrderRefundStatus.REFUNDING);//退款中
                orderItemRepository.save(orderItem);//退款中

                orderRefund.setOrderRefundStatus(OrderRefundStatus.REFUNDING);//退款中
                orderRefundRepository.save(orderRefund);
                //保存操作日志
                orderActionLogService.saveOrderActionLog(orderMain, orderItem, OrderActionType.ORDER_REFUND, user, OrderRefundStatus.REFUNDING.toString(), "卖家已确认收货,退款中");

                //退款单生成
                BigDecimal refundAmount = orderRefund.getRefundAmount();
                refundAmount = PayUtil.setOrderAmount(wxPayProperties, refundAmount, 1);
                FinanceRefund financeRefund = new FinanceRefund();
                financeRefund.setRefundAmount(refundAmount);
                BigDecimal chargeAmount = orderRefund.getChargeAmount();
                financeRefund.setChargeAmount(chargeAmount);
                financeRefund.setOrderRefund(orderRefund);
                financeRefund.setPayChannel(PayChannel.WECHAT);
                financeRefund.setPayStatus(PayStatus.WAITING);
                financeRefund.setPayRefundChannel(PayRefundChannel.ORIGINAL);//原路退回
                financeRefund.setCode("FRF" + String.valueOf(sequence.nextId()));//收款单编号
                financeRefund.setUser(user);
                financeRefund.setCompany(orderMain.getCompany());
                financeRefundService.save(financeRefund, user);

                /*
                退还库存
                 */
                inventoryOperationLogService.recordGoodsStockLog(orderItem.getGoodsDetail(), orderItem.getQuantity(), InventoryOperate.UNRENT, user);

                /*
                发起微信退款请求
                 */
                BigDecimal realRefund = refundAmount.subtract(chargeAmount);
                logger.info("实际退款金额：{}", realRefund);
                /*
                本地开发环境，不发起微信退款请求
                */
                if (!wxPayProperties.isUseDevEnv()) {
                    if (realRefund.compareTo(BigDecimal.ZERO) < 1) {
                        logger.info("实际退款金额【{}】<=0，不需要发起退款", realRefund);
                        completeRefund(orderRefund, orderItem, orderMain, financeRefund, null, "无退款金额", "无退款金额");
                        return;
                    }
                    String notifyUrl = wxPayProperties.getRefundNotifyUrl();
                    logger.info("退款通知Url:{}", notifyUrl);
                    WxPayRefundRequest wxPayRefundRequest = WxPayRefundRequest.newBuilder()
                            .outRefundNo(financeRefund.getId() + "")
                            .transactionId(financeReceivable.getTransactionCode())
                            .totalFee(MoneyUtils.yuanToFen(financeReceivable.getPayAmount()))
                            .refundFee(MoneyUtils.yuanToFen(realRefund))
                            .notifyUrl(notifyUrl)
                            .build();

                    try {
                        WechatUtil.setSandboxSignKey(wxPayProperties, wxPayService);
                        WxPayRefundResult refund = wxPayService.refund(wxPayRefundRequest);
                        logger.info("退款请求结果:{}", refund);
                    } catch (WxPayException e) {
                        logger.error("微信退款异常,订单号：{}，原因：{}", financeRefund.getId(), e.getMessage());
                        throw new SystemException(ExceptionCode.INVALID_WECHAT_PAY_REQUEST, "无效的微信支付请求!");
                    }
                } else {
                    logger.info("本地开发环境模拟退款");
                    completeRefund(orderRefund, orderItem, orderMain, financeRefund, "test", "退款测试", "退款成功");

                }


                break;
            case SUCCESS:
                break;
            case REFUNDING:
                break;
            case SELLER_SEND_GOODS:
                break;
            default:
                throw new SystemException(ExceptionCode.CANNOT_CANCEL_ORDER, "无效订单状态");
        }

    }

    private void completeRefund(OrderRefund orderRefund, OrderItem orderItem, OrderMain orderMain, FinanceRefund financeRefund, String transactionCode, String desc, String refundMsg) {
    /*
    更新退款单状态
     */
        orderRefund.setOrderRefundStatus(OrderRefundStatus.SUCCESS);
                    /*
                    更新订单明细的退款状态
                     */

        orderItem.setOrderRefundStatus(OrderRefundStatus.SUCCESS);
        if (isAllOrderItemsRefundFinished(orderItem, orderMain)) {
            /*
            更新主订单状态为完成
             */
            orderMain.setStatus(OrderMainStatus.FINISHED);
            orderActionLogService.saveOrderActionLog(financeRefund.getOrderRefund().getOrderMain(), financeRefund.getOrderRefund().getOrderItem(), OrderActionType.ORDER_MAIN, financeRefund.getUser(), OrderRefundStatus.SUCCESS.toString(), "已完成");
        }
                    /*
                    退货成功一次算一次购买行为，积分相应增加一次
                     */
        membershipPointService.addPoint(MembershipPointType.BUY, orderMain.getCompany(), orderMain.getUser());
                    /*
                    更新财务退款单状态
                     */
        financeRefund.setTransactionCode(transactionCode);
        financeRefund.setPayTime(new Date());
        financeRefund.setPayStatus(PayStatus.SUCCESS);
        financeRefund.setDescription(desc);
        financeRefundService.save(financeRefund, null);
                    /*
                    保存退款成功日志
                     */
        orderActionLogService.saveOrderActionLog(financeRefund.getOrderRefund().getOrderMain(), financeRefund.getOrderRefund().getOrderItem(), OrderActionType.ORDER_REFUND, financeRefund.getUser(), OrderRefundStatus.SUCCESS.toString(), refundMsg);
    }


    @Transactional
    public String resultnotify(String xmlResult) {
        if (xmlResult == null) {
            return null;
        }
        logger.info("退款结果XML:{}", xmlResult);
        try {
            WxPayRefundNotifyResult wxPayRefundNotifyResult = wxPayService.parseRefundNotifyResult(xmlResult);
            logger.info("退款结果：{}", wxPayRefundNotifyResult);
            if (wxPayRefundNotifyResult != null && wxPayRefundNotifyResult.getReturnCode().equals("SUCCESS")) {
                WxPayRefundNotifyResult.ReqInfo reqInfo = wxPayRefundNotifyResult.getReqInfo();
                if (reqInfo != null) {
                    String outRefundNo = reqInfo.getOutRefundNo();
                    FinanceRefund financeRefund = financeRefundService.findOne(Long.parseLong(outRefundNo));
                    if (financeRefund == null) {
                        logger.error("退款日志id:{}不存在", outRefundNo);
                        return WxPayNotifyResponse.success("退款ID不存在");
                    }
                    if (!financeRefund.getPayStatus().equals(PayStatus.WAITING)) {
                        logger.error("退款日志ID:{},状态不是待支付", outRefundNo);
                        return WxPayNotifyResponse.success("退款状态异常");
                    }
                    OrderRefund orderRefund = financeRefund.getOrderRefund();
                    if (orderRefund == null) {
                        logger.error("退款记录无对应退款单，退款ID:{}", outRefundNo);
                        return WxPayNotifyResponse.success("退款异常");
                    }
                    OrderRefundStatus orderRefundStatus = orderRefund.getOrderRefundStatus();
                    if (!orderRefundStatus.equals(OrderRefundStatus.REFUNDING)) {
                        logger.error("退款单状态为[{}]，退款日志ID:{}", orderRefundStatus, outRefundNo);
                        return WxPayNotifyResponse.success("退款异常");
                    }
                    OrderItem orderItem = orderRefund.getOrderItem();
                    OrderRefundStatus itemOrderRefundStatus = orderItem.getOrderRefundStatus();
                    if (!itemOrderRefundStatus.equals(OrderRefundStatus.REFUNDING)) {
                        logger.error("订单明细状态为[{}]，退款日志ID:{}", itemOrderRefundStatus, outRefundNo);
                        return WxPayNotifyResponse.success("退款异常");
                    }

                    Integer fen = MoneyUtils.yuanToFen(financeRefund.getRefundAmount().subtract(financeRefund.getChargeAmount()));

                    String description = "退款成功";
                    Integer refundFee = reqInfo.getRefundFee();
                    if (!fen.equals(refundFee)) {
                        description = description + ",金额异常:" + refundFee;
                    }
                    /*
                    更新退款单状态
                     */
                    orderRefund.setOrderRefundStatus(OrderRefundStatus.SUCCESS);
                    /*
                    更新订单明细的退款状态
                     */

                    orderItem.setOrderRefundStatus(OrderRefundStatus.SUCCESS);
                    /*
                    检查订单退货状态
                     */
                    OrderMain orderMain = orderRefund.getOrderMain();
                    if (isAllOrderItemsRefundFinished(orderItem, orderMain)) {
                        /*
                        更新主订单状态为完成
                         */
                        orderMain.setStatus(OrderMainStatus.FINISHED);
                        orderActionLogService.saveOrderActionLog(financeRefund.getOrderRefund().getOrderMain(), financeRefund.getOrderRefund().getOrderItem(), OrderActionType.ORDER_MAIN, financeRefund.getUser(), OrderRefundStatus.SUCCESS.toString(), "已完成");
                    }
                    /*
                    退货成功一次算一次购买行为，积分相应增加一次
                     */
                    membershipPointService.addPoint(MembershipPointType.BUY, orderMain.getCompany(), orderMain.getUser());
                    /*
                    更新财务退款单状态
                     */
                    financeRefund.setTransactionCode(reqInfo.getTransactionId());
                    financeRefund.setPayTime(DateUtils.parseDateStr(reqInfo.getSuccessTime()));
                    financeRefund.setPayStatus(PayStatus.SUCCESS);
                    financeRefund.setDescription(reqInfo.getRefundRecvAccout() + ",金额:" + refundFee);
                    financeRefundService.save(financeRefund, null);
                    /*
                    保存退款成功日志
                     */
                    orderActionLogService.saveOrderActionLog(financeRefund.getOrderRefund().getOrderMain(), financeRefund.getOrderRefund().getOrderItem(), OrderActionType.ORDER_REFUND, financeRefund.getUser(), OrderRefundStatus.SUCCESS.toString(), description);
                    return WxPayNotifyResponse.success("退款成功");
                } else {
                    logger.error("退款结果参数为空");
                    return WxPayNotifyResponse.success("退款失败");
                }
            } else {
                return WxPayNotifyResponse.success("退款失败");
            }
        } catch (Exception e) {
            logger.error("微信退款回调结果异常,异常原因{}", e.getMessage(), e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    private boolean isAllOrderItemsRefundFinished(OrderItem orderItem, OrderMain orderMain) {
        List<OrderItem> orderItems = orderMain.getOrderItems();
        for (OrderItem item : orderItems) {
            if (item.getId().equals(orderItem.getId())) {
                continue;
            }
            OrderRefundStatus refundStatus = item.getOrderRefundStatus();
            if (refundStatus == null) {
                /*
                未进行退货
                 */
                logger.info("订单明细ID:{} 还未进行退货", item.getId());
                return false;
            }
            if (!refundStatus.equals(OrderRefundStatus.SUCCESS)) {
                /*
                正进行退货流程
                 */
                logger.info("订单明细ID:{}  退货未完成,状态:{}", item.getId(), refundStatus);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(1).compareTo(BigDecimal.ZERO)<1);
        System.out.println(new BigDecimal(0).compareTo(BigDecimal.ZERO)<1);
        System.out.println(new BigDecimal(-1).compareTo(BigDecimal.ZERO)<1);
    }

}
