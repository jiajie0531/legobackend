package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

/** 接收角色权限
 * Created by xzm on 2017/10/19.
 */
@Setter
@Getter
public class ResourceInfo {
    @NotEmpty(message = "角色不能为空")
    private Long roleId;
    private Set<Long> menus;
    private Set<Long> buttons;
}
