package com.delllogistics.dto.goods;

import com.delllogistics.dto.BaseSearchModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsSearch extends BaseSearchModel {

    /**
     * 商品名
     */
    private String name;
    /**
     * id集合
     */
    private Long[] goodsIds;
    /**
     * 系列ID
     */
    private String goodsSeriesId;


}
