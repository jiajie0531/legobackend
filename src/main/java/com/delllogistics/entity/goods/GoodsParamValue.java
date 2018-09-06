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
@Table(name = "goods_param_value", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"goods_param_id", "value"})
})
public class GoodsParamValue extends BaseModel {


    /**
     * 商品参数配置表
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="goods_param_id")
    private GoodsParam goodsParam;


    /**
     * 参数值
     */
    @NotNull(message = "参数值不能为空")
    @Size(min = 1, max = 20, message = "参数值长度必须在1和20之间")
    @Column(length = 32)
    private String value;



    /**
     * 排序
     */
    @OrderBy
    @Column(nullable = false, columnDefinition = "int default 0")
    private int sort;




}
