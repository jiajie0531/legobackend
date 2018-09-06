package com.delllogistics.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Sell Goods Amount.<br/>
 * User: jiajie<br/>
 * Date: 04/03/2018<br/>
 * Time: 1:39 PM<br/>
 */
@Getter
@Setter
public class SellGoodsAmount {
    private long goodsId;
    private BigDecimal quantity;
    private String goodsName;
    private String goodsPicUrl;
    private String companyName;
}
