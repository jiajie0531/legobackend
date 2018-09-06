package com.delllogistics.controller.sys;

import com.delllogistics.entity.sys.SystemLog;
import com.delllogistics.service.sys.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**系统日志
 * Created by xzm on 2018-3-14.
 */
@RestController
@RequestMapping("/systemLog")
public class SystemLogController {

    private final SystemLogService systemLogService;

    @Autowired
    public SystemLogController(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    @PostMapping("/findList")
    public Page<SystemLog> findList(int page, int size){
        return systemLogService.findList(page,size);
    }
}
