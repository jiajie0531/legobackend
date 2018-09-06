package com.delllogistics.dto;

import com.delllogistics.entity.goods.Goods;
import lombok.Getter;
import lombok.Setter;

/**
 * 足迹的合计值.<br/>
 * User: jiajie<br/>
 * Date: 24/12/2017<br/>
 * Time: 10:24 AM<br/>
 */
@Setter
@Getter
public class BrowsingHistoryWithGoods {
    private Goods goods;
    private Long count;
}
