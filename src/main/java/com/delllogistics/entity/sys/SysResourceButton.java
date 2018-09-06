package com.delllogistics.entity.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 按钮资源
 * Created by jiajie on 14/10/2017.
 */
@Entity
@Getter
@Setter
public class SysResourceButton extends BaseModel {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int status;

    private String icon;

    @OrderBy
    private int sort;

    private String permission;
    /**所属菜单*/
    @JSONField(serialize = false)
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name="menu_id")
    private SysResourceMenu menu;

    @ManyToMany(mappedBy = "buttons",fetch = FetchType.LAZY)
    @JSONField(serialize = false)
    private Set<SysResourceRole> roles;

}
