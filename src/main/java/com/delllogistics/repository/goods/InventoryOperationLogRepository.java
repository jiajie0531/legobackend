package com.delllogistics.repository.goods;

import com.delllogistics.entity.goods.InventoryOperationLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * 库存操作记录.<br/>
 * User: jiajie<br/>
 * Date: 17/03/2018<br/>
 * Time: 11:46 AM<br/>
 */
@Repository
public interface InventoryOperationLogRepository
        extends PagingAndSortingRepository<InventoryOperationLog, Long>, JpaSpecificationExecutor<InventoryOperationLog> {
}
