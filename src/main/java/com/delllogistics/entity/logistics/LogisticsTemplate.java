package com.delllogistics.entity.logistics;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.LogisticsDeliveryTime;
import com.delllogistics.entity.enums.LogisticsFreightType;
import com.delllogistics.entity.enums.LogisticsPriceType;
import com.delllogistics.entity.enums.LogisticsDeliveryType;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysArea;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 *  物流模版
 * Created by calvin  2017/12/18
 */
@SQLDelete(sql = "update logistics_template set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
@Table(name = "logistics_template", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "company_id" })
})
public class LogisticsTemplate extends BaseModel {
    /**
     * 物流模版名称
     */
    @NotNull(message = "物流模版名称不能为空")
    @Size(min = 5, max = 60, message = "物流模版名称长度必须在5和60之间")
    @Column(nullable = false)
    private String name;

    /**
     * 发货时间
     */
    @NotNull(message = "发货时间不能为空")
    @Column(length = 32,nullable = false)
    @Enumerated(EnumType.STRING)
    @Convert(converter = LogisticsDeliveryTime.Convert.class)
    private LogisticsDeliveryTime deliveryTime;

    /**
     * 运送方式
     */
    @NotNull(message = "运送方式不能为空")
    @Column(length = 200,nullable = false)
    private String  deliveryType;


    /**
     * 运费类型
     */
    @NotNull(message = "运费类型不能为空")
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsFreightType freightType;

    /**
     * 计费类型
     */
    @NotNull(message = "计费类型不能为空")
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsPriceType priceType;

    /**
     * 宝贝地址
     */
    @NotNull(message = "宝贝地址不能为空")
    @JoinColumn(name = "area_id",nullable = false)
    @ManyToOne(fetch= FetchType.LAZY)
    private SysArea area;

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

    /**
     * 模版明细
     */
    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="logistics_template_id")
    private Set<LogisticsTemplateItem> logisticsTemplateItems;




}
