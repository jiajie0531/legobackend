package com.delllogistics.service.sys;

import com.delllogistics.entity.sys.SystemLog;
import com.delllogistics.repository.sys.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**系统日志
 * Created by xzm on 2018-3-13.
 */
@Service
public class SystemLogService {

    private final SystemLogRepository systemLogRepository;

    @Autowired
    public SystemLogService(SystemLogRepository systemLogRepository) {
        this.systemLogRepository = systemLogRepository;
    }

    @Transactional
    public void save(SystemLog systemLog){
        systemLogRepository.save(systemLog);
    }

    public Page<SystemLog> findList(int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return systemLogRepository.findAll(pageable);
    }
}
