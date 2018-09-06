package com.delllogistics.service.order;

import com.delllogistics.entity.enums.SellGoodsDateType;
import com.delllogistics.dto.order.SellGoodsAmount;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.repository.order.OrderItemRepository;
import com.delllogistics.repository.goods.GoodsRepository;
import com.delllogistics.util.DateUtils;
import com.delllogistics.util.MathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Order Item.<br/>
 * User: jiajie<br/>
 * Date: 04/03/2018<br/>
 * Time: 12:15 PM<br/>
 */
@Service
public class OrderItemService {

    private final GoodsRepository goodsRepository;

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, GoodsRepository goodsRepository) {
        this.orderItemRepository = orderItemRepository;
        this.goodsRepository = goodsRepository;
    }

    /**
     * get started time
     * @param sellGoodsDateType sellGoodsDateType
     * @return  startedTime
     */
    private String getStartedTime(SellGoodsDateType sellGoodsDateType){
        String startedTime;

        switch (sellGoodsDateType) {
            case THIS_WEEK:
                startedTime = DateUtils.getMondayOfThisWeek() + " 00:00:00";
                break;
            case THIS_MONTH:
                startedTime = DateUtils.getFirstDayOfThisMonth() + " 00:00:00";
                break;
            default:
                startedTime = DateUtils.getMondayOfThisWeek() + " 00:00:00";
                break;
        }
        return startedTime;
    }

    public ArrayList<SellGoodsAmount> findSellCount(SellGoodsDateType sellGoodsDateType) {
        ArrayList<SellGoodsAmount> sellGoodsAmountArrayList = new ArrayList<>();

        Object[][] objArr = orderItemRepository.findSellCount(getStartedTime(sellGoodsDateType));

        for (Object[] anObjArr : objArr) {//控制每个一维数组
            SellGoodsAmount sellGoodsAmount = new SellGoodsAmount();
            Long goodsId = Long.parseLong(anObjArr[0].toString());

            sellGoodsAmount.setGoodsId(goodsId);
            sellGoodsAmount.setQuantity(MathUtils.getBigDecimal(anObjArr[1]));

            Goods goods = goodsRepository.findOne(goodsId);
            sellGoodsAmount.setGoodsName(goods.getName());
            sellGoodsAmount.setGoodsPicUrl(goods.getGoodsPic().getUrl());
            sellGoodsAmount.setCompanyName(goods.getCompany().getName());

            sellGoodsAmountArrayList.add(sellGoodsAmount);
        }

        return sellGoodsAmountArrayList;
    }
}
