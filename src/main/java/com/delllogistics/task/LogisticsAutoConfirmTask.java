package com.delllogistics.task;

import com.delllogistics.service.LogisticsDeliveryService;

/**
 * 物流自动确认任务
 */
public class LogisticsAutoConfirmTask implements Runnable{

    private LogisticsDeliveryService logisticsDeliveryService;

    private Long orderId;

    public LogisticsAutoConfirmTask(LogisticsDeliveryService logisticsDeliveryService, Long orderId){
        this.logisticsDeliveryService=logisticsDeliveryService;
        this.orderId=orderId;
    }

    @Override
    public void run() {
        logisticsDeliveryService.deliveryOrder(orderId,null);
    }
}
