package com.delllogistics.repository.logistics;

import com.delllogistics.entity.logistics.LogisticsTemplateItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsTemplateItemRepository
        extends PagingAndSortingRepository<LogisticsTemplateItem, Long>, JpaSpecificationExecutor<LogisticsTemplateItem> {

    int deleteByLogisticsTemplate_Id(Long id);


}

