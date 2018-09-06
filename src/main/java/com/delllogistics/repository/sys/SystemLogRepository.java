package com.delllogistics.repository.sys;

import com.delllogistics.entity.sys.SystemLog;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by xzm on 2018-3-13.
 */
public interface SystemLogRepository extends PagingAndSortingRepository<SystemLog, Long> {
    @Override
    SystemLog save(SystemLog systemLog);
}
