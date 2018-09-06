package com.delllogistics.entity.goods;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.SysFile;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *  商品系列
 * Created by calvin  2017/11/16
 */
@SQLDelete(sql = "update goods_series set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class GoodsSeries extends BaseModel {
    /**
     * 系列名称
     * 系列名称不能为空，而且长度必须在1和30之间
     */
    @NotNull(message = "系列名称不能为空")
    @Size(min = 1, max = 30, message = "系列名称长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 英文名称
     * 英文名称不能为空，而且长度必须在1和30之间
     */
    @NotNull(message = "英文名称不能为空")
    @Size(min = 1, max = 30, message = "英文名称长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String enName;


    /**
     * 链接
     * 链接不能为空
     */
    @NotNull(message = "链接不能为空")
    @Column(nullable = false, unique = true)
    private String links;

    /**
     * 系列照片；
     */
    @OneToOne(fetch = FetchType.LAZY )
    @JoinColumn
    @Valid
    private SysFile descriptionPic;


    /**
     * Logo
     */
    @OneToOne(fetch = FetchType.LAZY )
    @JoinColumn
    @Valid
    private SysFile logoPic;


    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    @Column(name = "sort")
    private Long sort;

    /**
     * 是否可用
     */
    @Column(nullable = false)
    private boolean isUsed;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }


}