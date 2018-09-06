package com.delllogistics.service.logistics;

import com.delllogistics.dto.logistics.LogisticsDeliveryLogSearch;
import com.delllogistics.entity.enums.LogisticsDeliveryStatus;
import com.delllogistics.entity.enums.OrderActionType;
import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.entity.logistics.LogisticsDeliveryLog;
import com.delllogistics.entity.logistics.LogisticsExpress;
import com.delllogistics.entity.order.OrderMain;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.BizExceptionEnum;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.GeneralException;
import com.delllogistics.exception.SystemException;
import com.delllogistics.repository.logistics.LogisticsDeliveryLogRepository;
import com.delllogistics.repository.logistics.LogisticsExpressRepository;
import com.delllogistics.repository.order.OrderMainRepository;
import com.delllogistics.service.LogisticsDeliveryService;
import com.delllogistics.service.order.OrderActionLogService;
import com.delllogistics.task.LogisticsAutoConfirmTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class LogisticsDeliveryLogService {
    private final LogisticsDeliveryLogRepository logisticsDeliveryLogRepository;
    private final LogisticsExpressRepository logisticsExpressRepository;
    private final OrderMainRepository orderMainRepository;
    private final OrderActionLogService orderActionLogService;
    private final ScheduledExecutorService scheduledExecutorService;
    private final LogisticsDeliveryService logisticsDeliveryService;

    @Value("${logistics.autoConfirmTime}")
    private Long logisticsAutoConfirmTime;

    @Autowired
    public LogisticsDeliveryLogService(LogisticsDeliveryLogRepository logisticsDeliveryLogRepository, LogisticsExpressRepository logisticsExpressRepository, OrderMainRepository orderMainRepository, OrderActionLogService orderActionLogService, ScheduledExecutorService scheduledExecutorService, LogisticsDeliveryService logisticsDeliveryService) {
        this.logisticsDeliveryLogRepository = logisticsDeliveryLogRepository;
        this.logisticsExpressRepository = logisticsExpressRepository;
        this.orderMainRepository = orderMainRepository;
        this.orderActionLogService = orderActionLogService;
        this.scheduledExecutorService = scheduledExecutorService;
        this.logisticsDeliveryService = logisticsDeliveryService;
    }

    @Transactional
    public void businessShipped(LogisticsDeliveryLog logistics, User user){
        if(!StringUtils.isEmpty(logistics.getLogisticsExpress()) && !StringUtils.isEmpty(logistics.getLogisticsExpress().getId()) ){
            LogisticsExpress logisticsCompany =  logisticsExpressRepository.findOne(logistics.getLogisticsExpress().getId());
            logistics.setLogisticsExpress(logisticsCompany);
        }
        if(StringUtils.isEmpty(logistics.getOrderMain()) && StringUtils.isEmpty(logistics.getOrderMain().getId()) ){
            throw new GeneralException(BizExceptionEnum.VERIFY_ERROR);
        }
        OrderMain orderMain = orderMainRepository.findOne(logistics.getOrderMain().getId());
        logistics.setOrderMain(orderMain);
        logistics.setDeliveryStatus(LogisticsDeliveryStatus.BUSINESS_SHIPPED);
        logistics.setRemarks("商家【"+user.getUsername()+"】已发货");
        logistics.setCreateUser(user);
        logisticsDeliveryLogRepository.save(logistics);
        sendLogistics(orderMain,user);
    }

    private void sendLogistics(OrderMain orderMain, User user) {
        OrderMainStatus status = orderMain.getStatus();
        if (status == OrderMainStatus.PAID) {
            //订单发货等待确认
            orderMain.setStatus(OrderMainStatus.WAIT_TO_DELIVERY);
            //保存操作日志
            orderActionLogService.saveOrderActionLog(orderMain,null, OrderActionType.ORDER_MAIN, user,OrderMainStatus.WAIT_TO_DELIVERY.toString(), "卖家已发货");
            orderMainRepository.save(orderMain);
            LogisticsAutoConfirmTask logisticsAutoConfirmTask = new LogisticsAutoConfirmTask(logisticsDeliveryService, orderMain.getId());
            scheduledExecutorService.schedule(logisticsAutoConfirmTask,logisticsAutoConfirmTime, TimeUnit.MINUTES);
        } else {
            throw new SystemException(ExceptionCode.CANNOT_DELIVERY_ORDER, "订单已无法确认收货!");
        }

    }







    public Page<LogisticsDeliveryLog> findAll(LogisticsDeliveryLogSearch logisticsDeliveryLogSearch) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(logisticsDeliveryLogSearch.getPage(), logisticsDeliveryLogSearch.getSize(), sort);
        Specification<LogisticsDeliveryLog> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(logisticsDeliveryLogSearch.getOrderMainId())) {
                predicates.add(criteriaBuilder.equal(root.get("orderMain").get("id"), logisticsDeliveryLogSearch.getOrderMainId()));
            }
            if (!StringUtils.isEmpty(logisticsDeliveryLogSearch.getOrderRefundId())) {
                predicates.add(criteriaBuilder.equal(root.get("orderRefund").get("id"), logisticsDeliveryLogSearch.getOrderRefundId()));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"),0));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return logisticsDeliveryLogRepository.findAll(specification, pageable);
    }










}
