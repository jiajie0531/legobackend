package com.delllogistics.dto;

import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysResourceMenu;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

@Getter
@Setter
public class BackendUser {

    private Long id;
    @NotEmpty(message="用户名不能为空")
    private String username;
    private String password;

    private Set<Long> rolesId;

    private String token;

    private Set<SysResourceMenu> menus;

    private Set<String> permissions;

    private Set<Long> companiesId;

    private Set<Company> companies;

    private String randomKey;

    private String qrcodePrev;
}
