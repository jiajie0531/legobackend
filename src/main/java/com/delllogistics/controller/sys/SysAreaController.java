package com.delllogistics.controller.sys;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.SysAreaDto;
import com.delllogistics.dto.sys.DtoSysArea;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.service.sys.SysAreaService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestJsonController
@RequestMapping("/sysArea")
public class SysAreaController {
    private final SysAreaService sysAreaService;

    @Autowired
    public SysAreaController(SysAreaService sysAreaService) {
        this.sysAreaService = sysAreaService;
    }

    /**
     * 获取子地址 Map返回
     * @param id id
     * @param level 级别
     * @return 返回结果集合
     */
    @GetMapping("/findChildByAntdCascader")
    public Result findChildByAntdCascader(Long id, int level) {
        if (StringUtils.isEmpty(id) ||  StringUtils.isEmpty(level) ) {
            return ResultUtil.error(-1, "数据格式不正确！");
        }
        List sysAreas = sysAreaService.findChildByAntdCascader(id,level);
        return ResultUtil.success(sysAreas);
    }
    @GetMapping("/findAllChildByAntdCascader")
    public Result findAllChildByAntdCascader(Long[] ids) {
        if (StringUtils.isEmpty(ids) ||  ids.length<1) {
            return ResultUtil.error(-1, "数据格式不正确！");
        }
        List sysAreas = sysAreaService.findAllChildByAntdCascader(ids);
        return ResultUtil.success(sysAreas);
    }

    @GetMapping("/findAllCity")
    public Result findAllCity() {
        List sysAreas = sysAreaService.findAllCity();
        return ResultUtil.success(sysAreas);
    }


}
