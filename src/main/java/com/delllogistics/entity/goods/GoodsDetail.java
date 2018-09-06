package com.delllogistics.entity.goods;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

/**
 *  商品详细
 * Created by calvin  2017/12/27
 */
@SQLDelete(sql = "update goods_detail set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class GoodsDetail extends BaseModel {

    /**
     * 排序
     */
    @OrderBy
    @Column(nullable = false, columnDefinition = "int default 0")
    private int sort;

    /**
     * 商品
     */
    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id",nullable = false)
    private Goods goods;


    /**
     * 商品属性明细
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="goods_detail_id")
    @Transient
    private Set<GoodsAttributeDetail> goodsAttributeDetail;

    /**
     * 库存数量
     */
    @DecimalMin(value = "0.00",message = "库存数量必须大于0")
    @Column(nullable = false)
    private BigDecimal stock;

    /**
     * 销售数量
     */
    @DecimalMin(value = "0.00",message = "销售数量必须大于0")
    @Column(nullable = false)
    private BigDecimal salesVolume;

    /**
     * 租赁价格；单位是 RMB／天；
     */
    @NotNull(message = "租赁价格不能为空")
    @DecimalMin(value = "0.00",message = "租赁价格必须大于0")
    @Column(nullable = false)
    private BigDecimal rentalPrice;

    /**
     * 押金；单位是 RMB／天；
     */
    @NotNull(message = "押金价格不能为空")
    @DecimalMin(value = "0.00",message = "押金价格必须大于0")
    @Column(nullable = false)
    private BigDecimal ensurePrice;

}
