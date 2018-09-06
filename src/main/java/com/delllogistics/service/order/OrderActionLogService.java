package com.delllogistics.service.order;

import com.delllogistics.entity.order.OrderActionLog;
import com.delllogistics.entity.order.OrderItem;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.enums.OrderActionType;
import com.delllogistics.repository.order.OrderActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OrderActionLogService {

    private  final OrderActionLogRepository orderActionLogRepository;

    @Autowired
    public OrderActionLogService( OrderActionLogRepository orderActionLogRepository) {
        this.orderActionLogRepository = orderActionLogRepository;
    }


    public void saveOrderActionLog(OrderMain orderMain, OrderItem orderItem, OrderActionType orderActionType, User user, String status, String remarks) {
        OrderActionLog orderActionLog=new OrderActionLog();
        orderActionLog.setUser(user);
        orderActionLog.setStatus(status);
        orderActionLog.setRemarks(remarks);
        orderActionLog.setOrderMain(orderMain);
        orderActionLog.setOrderItem(orderItem);
        orderActionLog.setOrderActionType(orderActionType);
        orderActionLogRepository.save(orderActionLog);
    }
    public Page<OrderActionLog> findAll(int page , int size, Long orderMainId, OrderActionType orderActionType) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page, size, sort);
        if(StringUtils.isEmpty(orderActionType)){
            return orderActionLogRepository.findByOrderMain_Id(orderMainId,pageable);
        }else{
            return orderActionLogRepository.findByOrderMain_IdAndOrderActionType(orderMainId,orderActionType,pageable);
        }
    }


}
