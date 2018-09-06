package com.delllogistics.runner;

import com.delllogistics.service.TaskService;
import com.delllogistics.service.app.AppOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** 启动项目时执行
 * Created by xzm on 2018-3-27.
 */
@Component
public class OrderTimeOutRunner implements ApplicationRunner{

    private final AppOrderService appOrderService;
    private final TaskService taskService;

    @Autowired
    public OrderTimeOutRunner(AppOrderService appOrderService, TaskService taskService) {
        this.appOrderService = appOrderService;
        this.taskService = taskService;
    }


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        appOrderService.checkTimeoutPayOrder();
        taskService.checkTimeoutConfirmOrder();
    }
}
