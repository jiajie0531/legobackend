package com.delllogistics.dto.app;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.enums.GoodsSort;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsSearch extends BaseSearchModel{


    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品系列
     */
    private Long[] goodsSeriesIds;

    /**
     * 综合排序
     */
    private GoodsSort goodsSort;

    /**
     * 标签
     */
    private String[] goodsTags;

    /**
     * 年龄
     */
    private String age;


}
