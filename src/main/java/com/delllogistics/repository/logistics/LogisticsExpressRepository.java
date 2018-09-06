package com.delllogistics.repository.logistics;

import com.delllogistics.entity.logistics.LogisticsExpress;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsExpressRepository
        extends PagingAndSortingRepository<LogisticsExpress, Long>, JpaSpecificationExecutor<LogisticsExpress> {

}

