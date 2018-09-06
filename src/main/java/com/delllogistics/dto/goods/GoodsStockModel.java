package com.delllogistics.dto.goods;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GoodsStockModel {

    private Long id;
    private BigDecimal addNums;
    private BigDecimal stock;
}
