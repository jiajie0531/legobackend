package com.delllogistics.entity.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Set;

/**
 * 角色
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update sys_resource_role set is_deleted=1,update_time=now() where id=? and version_=?")
public class SysResourceRole extends BaseModel {
    /**
     * 名称
     */
    @NotEmpty(message="名称不能为空")
    @Column(unique = true,nullable = false)
    private String name;
    /**
     * 角色描述
     */
    @NotEmpty(message = "描述不能为空")
    private String description;

    @JSONField(serialize = false)
    @ManyToMany
    @JoinTable(name="SysResourceMenuRole",joinColumns=@JoinColumn(name="sys_resource_role_id"),
            inverseJoinColumns=@JoinColumn(name="sys_resource_menu_id"))
    private Set<SysResourceMenu> menus;

    @JSONField(serialize = false)
    @ManyToMany
    @JoinTable(
            name = "SysResourceButtonRole",
            joinColumns = @JoinColumn(name = "sys_resource_role_id"),
            inverseJoinColumns = @JoinColumn(name = "sys_resource_button_id")
    )
    private Set<SysResourceButton> buttons;

    @OneToOne
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;
}
