package com.delllogistics.repository.logistics;

import com.delllogistics.entity.logistics.LogisticsTemplate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsTemplateRepository
        extends PagingAndSortingRepository<LogisticsTemplate, Long>, JpaSpecificationExecutor<LogisticsTemplate> {

}

