package com.delllogistics.dto.goods;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.sys.SysGoodsCategory;
import com.delllogistics.entity.enums.GoodsParamType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class GoodsParamSearch extends BaseSearchModel {


    /**
     * 参数名
     */
    private String name;
    /**
     * 系统商品分类
     */
    private SysGoodsCategory sysGoodsCategory;
    /**
     * 是否可用
     */
    private Integer isUsed;

    /**
     * 商品参数类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private GoodsParamType goodsParamType;


}
