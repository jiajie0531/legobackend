package com.delllogistics.entity.sys;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 *  所在地区
 * Created by calvin  2017/12/06
 */
@Entity
@Getter
@Setter
public class SysArea  extends BaseModel {

    /**
     * 名称
     */
    @NotNull(message = "中文名称不能为空")
    @Size(min = 1, max = 30, message = "中文名称长度必须在1和30之间")
    @Column(nullable = false, length=60,unique = true)
    private String name;

    /**
     * 邮编
     */
    @Column(length=10,nullable = false, unique = true)
    private String code;

    /**
     * 等级
     */
    @NotNull(message = "等级不能为空")
    @DecimalMin(value = "0", message = "等级必须大于0")
    private Integer level;

    /**
     * 排序
     */
    @OrderBy
    @Column(nullable = false, columnDefinition = "int default 0")
    private int sort;

    /**
     * 是否可用
     */
    @NotNull(message = "是否可用不能为空")
    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean isUsed;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }


    /**父菜单*/
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name="parent_id")
    private SysArea parent;

    /**子菜单*/
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    @OrderBy("sort")
    private Set<SysArea> children;

}
