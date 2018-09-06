package com.delllogistics.entity.goods;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.InventoryCode;
import com.delllogistics.entity.enums.InventoryOperate;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsDetail;
import com.delllogistics.entity.sys.Company;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 库存操作记录表.<br/>
 * User: jiajie<br/>
 * Date: 22/12/2017<br/>
 * Time: 7:57 AM<br/>
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update inventory_operation_log set is_deleted=1,update_time=now() where id=? and version_=?")
public class InventoryOperationLog extends BaseModel {
    /**
     * goods id
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST})
    @JoinColumn(name = "goods_id",nullable = false)
    private Goods goods;

    /**
     * goods detail id
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST})
    @JoinColumn(name = "goods_detail_id",nullable = false)
    private GoodsDetail goodsDetail;

    /**
     * 期初库存数量
     */
    @NotNull(message = "期初库存数量不能为空")
    @DecimalMin(value = "0.00", message = "期初库存数量必须大于0")
    @Column(length = 32,nullable = false)
    private BigDecimal initQuantity;

    /**
     * 库存数量
     */
    @NotNull(message = "库存数量不能为空")
    @DecimalMin(value = "0.00", message = "库存数量必须大于0")
    @Column(length = 32,nullable = false)
    private BigDecimal quantity;

    /**
     * 本次操作数量
     */
    @NotNull(message = "本次操作数量不能为空")
    @Column(length = 32,nullable = false)
    private BigDecimal operateQuantity;

    /**
     * 库存操作类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private InventoryOperate operate;

    /**
     * 库存类型代码
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private InventoryCode code;



}
