package com.delllogistics.entity.logistics;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.LogisticsDeliveryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 物流模版明细
 */
@Entity
@Getter
@Setter
public class LogisticsTemplateItem extends BaseModel {




    /**
     * 关联模版
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="logistics_template_id",nullable = false)
    private LogisticsTemplate logisticsTemplate;

    /**
     * 运送方式
     */
    @NotNull(message = "运送方式不能为空")
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsDeliveryType deliveryType;

    /**
     * 送达到
     */
    @Lob
    @Column(columnDefinition="TEXT",nullable = false)
    private String deliveryTo;

    /**
     * 首价
     */
    @NotNull(message = "首价不能为空")
    @DecimalMin(value = "0.00",message = "首价必须大于等于0")
    @DecimalMax(value = "9999.00",message = "首价必须小于9999")
    @Column(nullable = false)
    private BigDecimal firstPrice;

    /**
     * 续价
     */
    @NotNull(message = "续价不能为空")
    @DecimalMin(value = "0.00",message = "续价必须大于等于0")
    @DecimalMax(value = "9999.00",message = "续价必须小于9999")
    @Column(nullable = false)
    private BigDecimal nextPrice;

    /**
     * 首量
     */
    @Range(min=1,max=99999,message = "首量必须1～99999之间")
    private  int firstValue;

    /**
     * 续量
     */
    @Range(min=1,max=99999,message = "续量必须1～99999之间")
    private  int nextValue;

    /**
     * 是否默认
     */
    private  boolean defualt;



}

