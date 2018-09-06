package com.delllogistics.controller;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.sys.SysResourceRole;
import com.delllogistics.service.sys.SysResourceRoleService;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final SysResourceRoleService sysResourceRoleService;

    @Autowired
    public RoleController(SysResourceRoleService sysResourceRoleService) {
        this.sysResourceRoleService = sysResourceRoleService;
    }

    @PostMapping("/findRoles")
    public Page<SysResourceRole> findRoles(int page, int size, SysResourceRole sysResourceRole){
        return sysResourceRoleService.findRoles(page,size, sysResourceRole);
    }

    @PostMapping("/findRoleList")
    public Iterable<SysResourceRole> findRoleList(SysResourceRole sysResourceRole){
        return sysResourceRoleService.findAll(sysResourceRole);
    }

    @PostMapping("/submitRole")
    public Result submitRole(@Valid SysResourceRole sysResourceRole, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.error(-1,bindingResult.getFieldError().getDefaultMessage());
        }
        sysResourceRoleService.submitRole(sysResourceRole);
        return ResultUtil.success();
    }

    @PostMapping("/delRole")
    public Result delRole(@RequestParam Long id){
        sysResourceRoleService.delRole(id);
        return ResultUtil.success();
    }
}
