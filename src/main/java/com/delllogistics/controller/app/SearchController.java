package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.goods.*;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.service.app.SearchService;
import com.delllogistics.service.goods.GoodsParamValueService;
import com.delllogistics.service.goods.GoodsTagService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Search.<br/>
 * User: jiajie<br/>
 * Date: 20/11/2017<br/>
 * Time: 7:36 AM<br/>
 */
@RestJsonController
@RequestMapping("/app/search")
public class SearchController {

    private final SearchService searchService;

    private final GoodsTagService goodsTagService;
    private final GoodsParamValueService goodsParamValueService;

    @Autowired
    public SearchController(SearchService searchService, GoodsTagService goodsTagService, GoodsParamValueService goodsParamValueService) {
        this.searchService = searchService;
        this.goodsTagService = goodsTagService;
        this.goodsParamValueService = goodsParamValueService;
    }

    /**
     * 搜索商品
     *
     * @param page      page index
     * @param size      page size
     * @param content   content
     * @param ages      ages
     * @param tag       tag
     * @param seriesIds series ids
     * @return Page
     */
    @GetMapping("/findGoods")
    @JsonConvert(
            type = Goods.class,
            includes = {"id", "name", "rentalPrice", "ensurePrice", "goodsPic", "goodsTags", "goodsSeries", "goodsDetail"},
            nest = {
                    @NestConvert(type = GoodsTag.class, includes = {"name", "icon"}),
                    @NestConvert(type = GoodsSeries.class, includes = {"name"}),
                    @NestConvert(type = SysFile.class, includes = "url"),
                    @NestConvert(type = GoodsDetail.class, includes = {"id", "stock", "rentalPrice", "ensurePrice", "salesVolume"}),
            })
    public Page<Goods> findGoods(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "0") int size,
                                 @RequestParam(defaultValue = "") String content,
                                 String[] ages,
                                 String[] tag,
                                 Long[] seriesIds) {
        Page<Goods> goodsPage = searchService.findGoods(page, size, null, content, ages, tag, seriesIds);
        return goodsPage;
    }

    /**
     * 最热商品
     *
     * @return List
     */
    @GetMapping("/findHotSearchs")
    public List<String> findHotSearchs() {
        return searchService.findHotSearchs();
    }


    /**
     * 根据标签搜索商品
     *
     * @return Page
     */
    @GetMapping("/findTagList")
    @JsonConvert(
            type = GoodsTag.class,
            includes = {"id", "name"}
    )
    public Page<GoodsTag> findTagList(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "0") int size) {
        return goodsTagService.findAll(page, size, null);
    }

    /**
     * 获取商品年龄参数
     * @return Page
     */
    @GetMapping("/findAgesList")
    @JsonConvert(
            type = GoodsParamValue.class,
            includes = {"id", "value"}
    )
    public Result findAgesList(@RequestParam Long goodsParamId) {
        List<GoodsParamValue> goodsParamValues =goodsParamValueService.findAllByGoodsParamId(goodsParamId);
        if(goodsParamValues.size()>0) {
            return ResultUtil.success(goodsParamValues);
        }else{
            return ResultUtil.error(-1,"没有获取到任何数据");
        }
    }


}
