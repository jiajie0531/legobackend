package com.delllogistics.entity.goods;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *  商品参数值可选范围
 * Created by calvin  2017/12/27
 */
@Entity
@Getter
@Setter
@Table(name = "goods_param_item", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"goods_param_id", "goods_id","value"})
})
public class GoodsParamItem extends BaseModel {
    /**
     * 关联商品参数配置表
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="goods_param_id",nullable = false)
    private GoodsParam goodsParam;
    /**
     * 关联商品
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="goods_id")
    private Goods goods;

    /**
     * 参数值
     */
    @NotNull(message = "参数值不能为空")
    @Size(min = 1, max = 500, message = "参数值长度必须在1和500之间")
    @Column(length = 500,nullable = false)
    private String value;


}
