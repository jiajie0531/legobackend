package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.goods.GoodsSearch;
import com.delllogistics.entity.goods.*;
import com.delllogistics.entity.logistics.LogisticsTemplate;
import com.delllogistics.entity.logistics.LogisticsTemplateItem;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.service.goods.GoodsService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author JiaJie
 * create 2017-11-27 6:06 PM
 **/
@RestJsonController
@RequestMapping("/app/goods")
public class AppGoodsController {

    private final GoodsService goodsService;

    @Autowired
    public AppGoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * 获取商品详细
     *
     * @param id
     * @return
     */
    @GetMapping("/findById")
    @JsonConvert(
            type = Goods.class,
            includes = {"id", "name", "stock",  "code", "roughWeight", "tagNo", "description",
                    "goodsBrand", "goodsDetail", "detailPics", "goodsOrigin",
                    "goodsSeries", "ensurePrice", "rentalPrice", "goodsPic", "goodsParamItems"},
            nest = {
                    @NestConvert(type = GoodsSeries.class, includes = {"id", "name"}),
                    @NestConvert(type = GoodsBrand.class, includes = {"id", "name"}),
                    @NestConvert(type = SysFile.class, includes = "url"),
                    @NestConvert(type = GoodsParamItem.class, includes = {"value", "goodsParam"}),
                    @NestConvert(type = GoodsParam.class, includes = {"name"}),
                    @NestConvert(type = GoodsDetail.class, includes = {"id", "stock", "rentalPrice", "ensurePrice", "salesVolume"}),
            })
    public Result findById(Long id) {

        Goods goods = goodsService.findById(id);
        return ResultUtil.success(goods);

    }

    /**
     * 查询商品物流模版
     * @param goodsSearch 搜索条件
     * @return 结果集
     */
    @PostMapping("/findLogisticTemplateByIds")
    @JsonConvert(
            type = Goods.class,
            includes = { "logisticsTemplate"},
            nest = {
                    @NestConvert(type = LogisticsTemplate.class, includes = {"freightType", "priceType", "logisticsTemplateItems"}),
                    @NestConvert(type = LogisticsTemplateItem.class, includes = {"deliveryType", "deliveryTo", "firstPrice", "nextPrice", "firstValue", "nextValue", "defualt"}),
            }
    )
    public Result findLogisticTemplateByIds(@RequestBody GoodsSearch goodsSearch) {
        if(goodsSearch.getGoodsIds().length<1){
            return ResultUtil.error(-1,"没有模版");
        }
        Iterable<Goods>  goods= goodsService.findLogisticTemplateByIds(goodsSearch);
        if(goods==null){
            return ResultUtil.error(-1,"没有模版");
        }
        return ResultUtil.success(goods);
    }
}
