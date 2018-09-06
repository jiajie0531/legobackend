package com.delllogistics.service;

import com.delllogistics.entity.enums.LogisticsDeliveryStatus;
import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.entity.logistics.LogisticsDeliveryLog;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.repository.logistics.LogisticsDeliveryLogRepository;
import com.delllogistics.repository.order.OrderMainRepository;
import com.delllogistics.task.LogisticsAutoConfirmTask;
import com.delllogistics.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TaskService {

    private final OrderMainRepository orderMainRepository;
    private final LogisticsDeliveryLogRepository logisticsDeliveryLogRepository;
    private final ScheduledExecutorService scheduledExecutorService;
    private final LogisticsDeliveryService logisticsDeliveryService;
    
    @Value("${logistics.autoConfirmTime}")
    private Long logisticsAutoConfirmTime;

    @Autowired
    public TaskService(OrderMainRepository orderMainRepository, LogisticsDeliveryLogRepository logisticsDeliveryLogRepository, ScheduledExecutorService scheduledExecutorService, LogisticsDeliveryService logisticsDeliveryService) {
        this.orderMainRepository = orderMainRepository;
        this.logisticsDeliveryLogRepository = logisticsDeliveryLogRepository;
        this.scheduledExecutorService = scheduledExecutorService;
        this.logisticsDeliveryService = logisticsDeliveryService;
    }

    public void checkTimeoutConfirmOrder(){
        List<OrderMain> orderMainList = orderMainRepository.findAllByIsDeletedAndStatus(false, OrderMainStatus.WAIT_TO_DELIVERY);
        log.info("检查超时确认收货订单,数量:{}",orderMainList.size());
        for (OrderMain orderMain : orderMainList) {
            Long id = orderMain.getId();
            LogisticsAutoConfirmTask autoConfirmTask = new LogisticsAutoConfirmTask(logisticsDeliveryService, id);
            Date now = new Date();
            /*
            查找订单发货时间
             */
            LogisticsDeliveryLog logisticsDeliveryLog = logisticsDeliveryLogRepository.findByOrderMain_IdAndAndDeliveryStatus(id, LogisticsDeliveryStatus.BUSINESS_SHIPPED);
            if(logisticsDeliveryLog==null){
                log.error("订单无发货日志，ID：{}",id);
                return;
            }
            Date outTime = DateUtils.addDateByMinute(logisticsDeliveryLog.getCreateTime(), logisticsAutoConfirmTime.intValue());
            log.info("处理未确认收货订单，id：{},超时时间：{}", id, outTime);
            if (now.after(outTime)) {
                /*
                已超过确认收货时间
                 */

                scheduledExecutorService.execute(autoConfirmTask);
            } else {
                /*
                未超时确认收货订单，增加延期确认收货任务
                 */
                long outMilliseconds = outTime.getTime() - now.getTime();
                scheduledExecutorService.schedule(autoConfirmTask, outMilliseconds, TimeUnit.MILLISECONDS);
            }
        }
    }
}
