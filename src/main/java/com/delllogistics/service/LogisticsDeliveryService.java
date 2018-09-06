package com.delllogistics.service;

import com.delllogistics.entity.enums.LogisticsDeliveryStatus;
import com.delllogistics.entity.enums.OrderActionType;
import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.entity.logistics.LogisticsDeliveryLog;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.logistics.LogisticsDeliveryLogRepository;
import com.delllogistics.repository.order.OrderMainRepository;
import com.delllogistics.service.order.OrderActionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class LogisticsDeliveryService {

    private final LogisticsDeliveryLogRepository logisticsDeliveryLogRepository;
    private final OrderMainRepository orderMainRepository;
    private final OrderActionLogService orderActionLogService;

    public LogisticsDeliveryService(LogisticsDeliveryLogRepository logisticsDeliveryLogRepository, OrderMainRepository orderMainRepository, OrderActionLogService orderActionLogService) {
        this.logisticsDeliveryLogRepository = logisticsDeliveryLogRepository;
        this.orderMainRepository = orderMainRepository;
        this.orderActionLogService = orderActionLogService;
    }

    @Transactional
    public void deliveryOrder(long orderId, User user) {
        log.info("订单确认收货，订单ID:{}",orderId);
        try {
            OrderMain orderMain = orderMainRepository.findOne(orderId);
            if (user!=null && !orderMain.getUser().getId().equals(user.getId())) {
                throw new SystemException(ExceptionCode.INVALID_DELIVERY_ORDER, "无效的确认收货请求!");
            }
            OrderMainStatus status = orderMain.getStatus();
            if (status == OrderMainStatus.WAIT_TO_DELIVERY) {
                //已发货等待确认收货
                orderMain.setDeliveryTime(new Date());
                orderMain.setStatus(OrderMainStatus.DELIVERY);


                //添加过期日期
                Date date = new Date();//取时间
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, orderMain.getDayNum());//把日期往后增加一天.整数往后推,负数往前移动
                List<OrderItem> list = new ArrayList<>();
                for (OrderItem orderItem : orderMain.getOrderItems()) {
                    orderItem.setExpiryTime(calendar.getTime());
                    list.add(orderItem);
                }
                orderMain.setOrderItems(list);
                orderMainRepository.save(orderMain);
                buyerDelivery(orderMain, user);
                //保存操作日志
                String remarks="买家已确认收货";
                if(user==null){
                    remarks="系统自动确认收货";
                }
                orderActionLogService.saveOrderActionLog(orderMain, null, OrderActionType.ORDER_MAIN, user, OrderMainStatus.DELIVERY.toString(), remarks);
            } else {
                throw new SystemException(ExceptionCode.CANNOT_DELIVERY_ORDER, "订单无法确认收货!");
            }
        } catch (Exception e) {
            throw new SystemException(ExceptionCode.CANNOT_DELIVERY_ORDER, "订单无法确认收货!");
        }


    }

    private void buyerDelivery(OrderMain orderMain, User user){
        LogisticsDeliveryLog logistics = logisticsDeliveryLogRepository.findByOrderMain_IdAndAndDeliveryStatus(orderMain.getId(), LogisticsDeliveryStatus.BUSINESS_SHIPPED);
        if(logistics!=null){
            LogisticsDeliveryLog deliveryLog  = new LogisticsDeliveryLog();
            deliveryLog.setDeliveryStatus(LogisticsDeliveryStatus.BUYER_DELIVERY);
            deliveryLog.setDeliveryType(logistics.getDeliveryType());
            deliveryLog.setOrderMain(orderMain);
            if(user==null){
                deliveryLog.setRemarks("系统自动确认收货");
            }else{
                deliveryLog.setRemarks("买家【"+user.getUsername()+"】已确认收货");
            }

            deliveryLog.setLogisticsCode(logistics.getLogisticsCode());
            deliveryLog.setLogisticsExpress(logistics.getLogisticsExpress());
            deliveryLog.setCreateUser(user);
            logisticsDeliveryLogRepository.save(deliveryLog);
        }

    }
}
