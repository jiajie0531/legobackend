package com.delllogistics.task;

import com.delllogistics.service.order.OrderTaskService;

/**订单支付超时任务
 * Created by xzm on 2018-3-27.
 */
public class OrderTimeOutTask implements Runnable{

    private OrderTaskService orderTaskService;

    private Long orderId;

    public OrderTimeOutTask(OrderTaskService orderTaskService,Long orderId){
        this.orderTaskService=orderTaskService;
        this.orderId=orderId;
    }

    @Override
    public void run() {
        orderTaskService.timeoutOrderPay(orderId);
    }
}
