package com.delllogistics.service.order;

import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.enums.InventoryOperate;
import com.delllogistics.entity.enums.OrderActionType;
import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.repository.order.OrderMainRepository;
import com.delllogistics.service.InventoryOperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTaskService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderMainRepository orderMainRepository;

    private final OrderActionLogService orderActionLogService;

    private final InventoryOperationLogService inventoryOperationLogService;

    @Autowired
    public OrderTaskService(OrderMainRepository orderMainRepository, OrderActionLogService orderActionLogService, InventoryOperationLogService inventoryOperationLogService) {
        this.orderMainRepository = orderMainRepository;
        this.orderActionLogService = orderActionLogService;
        this.inventoryOperationLogService = inventoryOperationLogService;
    }


    @Transactional
    public void timeoutOrderPay(long orderId){
        logger.info("开始订单超时处理,订单ID：{}",orderId);
        OrderMain orderMain = orderMainRepository.findOne(orderId);
        if(orderMain==null){
            logger.error("无效的订单ID：{}",orderId);
            return;
        }
        OrderMainStatus status = orderMain.getStatus();
        if(status.equals(OrderMainStatus.WAIT_TO_PAY)){
            /*
            只允许自动取消超时未支付的订单
             */
            User user = orderMain.getUser();
            orderMain.setStatus(OrderMainStatus.CANCELED);
            orderActionLogService.saveOrderActionLog(orderMain,null, OrderActionType.ORDER_MAIN, user,OrderMainStatus.CANCELED.toString(), "超时未支付");
            orderMainRepository.save(orderMain);
            //todo 退还库存
            List<OrderItem> orderItems = orderMain.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                inventoryOperationLogService.recordGoodsStockLog(orderItem.getGoodsDetail(),orderItem.getQuantity(), InventoryOperate.TIMEOUT_ORDER,null);
            }
        }else{
            logger.info("订单已被处理,状态为：{}，id:{}",status,orderId);
        }
    }


}
