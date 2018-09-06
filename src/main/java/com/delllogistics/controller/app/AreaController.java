package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.SysAreaDto;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.service.sys.SysAreaService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@RestJsonController
@RequestMapping("app/area")
public class AreaController {
    private final SysAreaService sysAreaService;

    @Autowired
    public AreaController(SysAreaService sysAreaService) {
        this.sysAreaService = sysAreaService;
    }

    /**
     * 获取收货子地址
     * @param id id
     * @param level 级别
     * @return 返回结果
     */
    @GetMapping("/findChildList")
    public Result findChildList(Long id, int level) {
        if (StringUtils.isEmpty(id) ||  StringUtils.isEmpty(level) ) {
            return ResultUtil.error(-1, "数据格式不正确！");
        }
        Set<SysAreaDto> sysAreas = sysAreaService.findChildList(id,level);
        return ResultUtil.success(sysAreas);
    }

    /**
     * 获取收货子地址 Map返回
     * @param id id
     * @param level 级别
     * @return 返回结果集合
     */
    @GetMapping("/findChildListMap")
    public Result findChildListMap(Long id, int level) {
        if (StringUtils.isEmpty(id) ||  StringUtils.isEmpty(level) ) {
            return ResultUtil.error(-1, "数据格式不正确！");
        }
        List sysAreas = sysAreaService.findChildListMap(id,level);
        return ResultUtil.success(sysAreas);
    }

    /**
     * 获取收货地址第一级
     * @return 返回结果
     */
    @GetMapping("/findParentList")
    @JsonConvert(type =SysArea.class,includes = {"id","name","level"})
    public Result findParentList() {
        List sysAreas = sysAreaService.findParentList();
        return ResultUtil.success(sysAreas);
    }


}
