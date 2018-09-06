package com.delllogistics.entity.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 菜单资源
 * Created by jiajie on 14/10/2017.
 */
@Entity
@Getter
@Setter
public class SysResourceMenu extends BaseModel {

    @Column(nullable = false)
    private String name;

    private String icon;

    private String url;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int status;

    @OrderBy
    private int sort;

    /**父菜单*/
    @JSONField(serialize = false)
    @ManyToOne
    @JoinColumn(name="parent_id")
    private SysResourceMenu parent;

    /**子菜单*/
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    @OrderBy("sort")
    private Set<SysResourceMenu> children;

    /**
     * 拥有的按钮
     */
    @OneToMany
    @JoinColumn(name="menu_id")
    private Set<SysResourceButton> buttons;

    @ManyToMany(mappedBy = "menus",fetch = FetchType.LAZY)
    @JSONField(serialize = false)
    private Set<SysResourceRole> roles;

    @Transient
    private Set<SysResourceMenu> subMenus;
}
