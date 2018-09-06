package com.delllogistics.dto.goods;

import com.delllogistics.dto.BaseSearchModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsParamValueSearch  extends BaseSearchModel{


    //商品参数配置表
    private Long goodsParamId;

}
