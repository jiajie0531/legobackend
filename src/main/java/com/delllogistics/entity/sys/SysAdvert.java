package com.delllogistics.entity.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *  广告管理
 * Created by calvin  2017/11/14
 */
@SQLDelete(sql = "update sys_advert set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
@Table(name = "sys_advert", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "name"})
})
public class SysAdvert extends BaseModel {
    /**
     * 广告名称
     */
    @NotNull(message = "广告名称不能为空")
    @Size(min = 1, max = 30, message = "广告名称长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String name;


    /**
     * 链接
     */
    @NotNull(message = "链接不能为空")
    @Size(min = 8, max = 100, message = "广告名称长度必须在18和100之间")
    @Column(nullable = false, unique = true)
    private String url;

    /**
     * 广告照片
     */
    @OneToOne
    @JoinColumn
    @Valid
    private SysFile descriptionPic;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    @Column(name = "sort")
    private Long sort;

    /**
     * 是否可用
     */
    @NotNull(message = "是否可用不能为空")
    private boolean isUsed;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }

    /**
     * 关联企业
     */
    @NotNull(message = "关联企业不能为空")
    @Valid
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;



}
