package com.delllogistics.repository.logistics;

import com.delllogistics.entity.enums.LogisticsDeliveryStatus;
import com.delllogistics.entity.logistics.LogisticsDeliveryLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsDeliveryLogRepository
        extends PagingAndSortingRepository<LogisticsDeliveryLog, Long>, JpaSpecificationExecutor<LogisticsDeliveryLog> {


    LogisticsDeliveryLog findByOrderMain_IdAndAndDeliveryStatus(Long orderMainId,LogisticsDeliveryStatus deliveryStatus);



}

