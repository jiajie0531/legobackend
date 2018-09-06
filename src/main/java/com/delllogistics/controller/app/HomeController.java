package com.delllogistics.controller.app;

import com.delllogistics.dto.sys.SysAdvertSearch;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsDetail;
import com.delllogistics.entity.goods.GoodsSeries;
import com.delllogistics.entity.goods.GoodsTag;
import com.delllogistics.entity.sys.SysAdvert;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.service.app.HomeService;
import com.delllogistics.service.goods.GoodsSeriesService;
import com.delllogistics.service.sys.SysAdvertService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by guangxingju on 2017/11/2.
 */
@RestJsonController
@RequestMapping("/app/home")
public class HomeController {
    private final SysAdvertService sysAdvertService;
    private final GoodsSeriesService goodsSeriesService;
    private final HomeService homeService;

    @Autowired
    public HomeController(SysAdvertService sysAdvertService, GoodsSeriesService goodsSeriesService, HomeService homeService) {
        this.sysAdvertService = sysAdvertService;
        this.goodsSeriesService = goodsSeriesService;
        this.homeService = homeService;
    }

    /**
     * 获取广告图
     *
     * @param sysAdvertSearch 分页，企业ID
     * @return Page
     */
    @GetMapping("/findAdvertList")
    @JsonConvert(
            type = SysAdvert.class,
            includes = {"id", "url", "sort", "name", "descriptionPic"},
            nest = {
                    @NestConvert(type = SysFile.class, includes = "url"),
            })
    public Page<SysAdvert> findAdvertList(SysAdvertSearch sysAdvertSearch) {
        return sysAdvertService.findAdvertList(sysAdvertSearch);
    }

    /**
     * 查询系列
     *
     * @param page page
     * @param size size
     * @return Page
     */
    @GetMapping("/findSeriesList")
    @JsonConvert(
            type = GoodsSeries.class,
            includes = {"id", "name", "enName", "links", "sort", "descriptionPic", "logoPic"},
            nest = {
                    @NestConvert(type = SysFile.class, includes = "url"),
            })
    public Page<GoodsSeries> findSeriesList(int page, int size) {
        GoodsSeries goodsSeries = new GoodsSeries();
        goodsSeries.setIsUsed(true);
        return goodsSeriesService.findAll(page, size, goodsSeries, true);
    }

    /**
     * 根据系列查询商品
     *
     * @param page      page
     * @param size      size
     * @param companyId company id
     * @param seriesId  series id
     * @return Page
     */
    @GetMapping("/findGoodsBySeries")
    @JsonConvert(
            type = Goods.class,
            includes = {"id", "name","stock",  "rentalPrice", "ensurePrice", "goodsPic", "goodsTags", "goodsSeries", "goodsDetail"},
            nest = {
                    @NestConvert(type = GoodsTag.class, includes = {"name", "icon"}),
                    @NestConvert(type = GoodsSeries.class, includes = {"name"}),
                    @NestConvert(type = SysFile.class, includes = "url"),
                    @NestConvert(type = GoodsDetail.class, includes = {"id", "stock", "rentalPrice", "ensurePrice", "salesVolume"}),
            })
    public Page<Goods> findGoodsBySeries(int page, int size, Long companyId, Long seriesId) {
        return homeService.findGoodsBySeries(page, size, companyId, seriesId);
    }


    /**
     * 根据标签查询商品
     *
     * @param type      type
     * @param companyId company id
     * @return Page
     */
    @GetMapping("/findGoodsByTag")
    @JsonConvert(
            type = Goods.class,
            includes = {"id","stock",  "name", "rentalPrice", "ensurePrice", "goodsPic", "goodsTags", "goodsSeries", "goodsDetail"},
            nest = {
                    @NestConvert(type = GoodsTag.class, includes = {"name", "icon"}),
                    @NestConvert(type = GoodsSeries.class, includes = {"name"}),
                    @NestConvert(type = SysFile.class, includes = "url"),
                    @NestConvert(type = GoodsDetail.class, includes = {"id", "stock", "rentalPrice", "ensurePrice", "salesVolume"}),
            })
    public Page<Goods> findGoodsByTag(String type, Long companyId) {
        return homeService.findMainGoods(0, 10, companyId, type);
    }


}
