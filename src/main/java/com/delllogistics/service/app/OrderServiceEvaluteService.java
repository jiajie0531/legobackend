package com.delllogistics.service.app;

import com.delllogistics.entity.enums.OrderServiceEvaluateStatus;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.order.OrderServiceEvaluate;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.order.OrderMainRepository;
import com.delllogistics.repository.order.OrderServiceEvaluateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单
 * Created by xzm on 2017-12-13.
 */
@Service
public class OrderServiceEvaluteService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private  final OrderServiceEvaluateRepository orderServiceEvaluateRepository;

    private final  OrderMainRepository orderMainRepository;

    public OrderServiceEvaluteService(OrderServiceEvaluateRepository orderServiceEvaluateRepository, OrderMainRepository orderMainRepository) {
        this.orderServiceEvaluateRepository = orderServiceEvaluateRepository;
        this.orderMainRepository = orderMainRepository;
    }


    @Transactional
    public int submit(OrderServiceEvaluate orderServiceEvaluate, User user) {
        int rst;
        try {

            OrderMain orderMain = orderMainRepository.findOne(orderServiceEvaluate.getOrderMain().getId());
            orderServiceEvaluate.setOrderMain(orderMain);
            orderServiceEvaluate.setEvaluateStatus(OrderServiceEvaluateStatus.SERVICE_EVALUTE_FINISHED);
            orderServiceEvaluate.setUser(user);
            orderServiceEvaluate.setUpdateUser(user);
            orderServiceEvaluateRepository.save(orderServiceEvaluate);

            orderMain.setOrderServiceEvaluateStatus(OrderServiceEvaluateStatus.SERVICE_EVALUTE_FINISHED);
            orderMainRepository.save(orderMain);
            rst = 1;
        }catch (Exception e){
            if(e instanceof SystemException){
                throw new SystemException(((SystemException) e).getCode(), e.getMessage());
            }
            logger.error("用户 {} 提交订单服务评价  发生错误 //r//n {} //r//n 提交数据 {}", user.getId(),e.toString(),orderServiceEvaluate);
            throw new SystemException(ExceptionCode.SYSTEM, "提交订单服务评价失败");
        }


        return rst;
    }











}
