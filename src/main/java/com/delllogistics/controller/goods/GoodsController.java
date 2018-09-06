package com.delllogistics.controller.goods;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.goods.GoodsSearch;
import com.delllogistics.dto.goods.GoodsStockModel;
import com.delllogistics.entity.goods.*;
import com.delllogistics.entity.logistics.LogisticsTemplate;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.goods.GoodsService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@RestJsonController
@RequestMapping("/goods")
public class GoodsController {


    private final GoodsService goodsService;


    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping("/findAll")
    @JsonConvert(
            type = Goods.class,
            includes = {"id", "name", "goodsSeries", "ensurePrice", "rentalPrice", "createTime", "goodsPic","goodsTags"},
            nest = {
                    @NestConvert(type = GoodsSeries.class, includes = {"name"}),
                    @NestConvert(type = GoodsTag.class, includes = {"name"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            }
     )
    public Page<Goods> findAll( @RequestBody GoodsSearch goodsSearch) {
        return goodsService.findAll(goodsSearch);
    }

    @PostMapping("/findById")
    @JsonConvert(
            type = Goods.class,
            includes = {"id", "name", "code", "roughWeight", "tagNo", "description",
                    "goodsBrand" , "goodsDetail", "detailPics", "goodsTags",
                    "goodsSeries", "ensurePrice", "rentalPrice", "goodsPic", "goodsParamItems", "logisticsTemplate"},
            nest = {
                    @NestConvert(type = GoodsSeries.class, includes = {"id", "name"}),
                    @NestConvert(type = GoodsParamItem.class, includes = {"id", "value","goodsParam"}),
                    @NestConvert(type = GoodsParam.class, includes = {"id", "name"}),
                    @NestConvert(type = GoodsTag.class, includes = {"id", "name"}),
                    @NestConvert(type = GoodsBrand.class, includes = {"id", "name"}),
                    @NestConvert(type = GoodsDetail.class, includes = {"id","stock"}),
                    @NestConvert(type = LogisticsTemplate.class, includes = {"id","name"}),
            })
    public Result findById(Long id) {
        Goods goods = goodsService.findById(id);
        return ResultUtil.success(goods);
    }

    @PostMapping("/findAllSelect")
    @JsonConvert(
            type = Goods.class,
            includes = {"id", "name", "goodsSeries", "ensurePrice", "rentalPrice", "createTime", "goodsPic"},
            nest = {
                    @NestConvert(type = GoodsSeries.class, includes = {"name"}),
                    @NestConvert(type = SysFile.class, includes = {"url"})
            })
    public Iterable<Goods> findAllSelect(int page, int size, Goods goods) {
        return goodsService.findAll(page, size, goods);
    }


    @PostMapping("/save")
    public Result save(@Valid @RequestBody Goods goods, BindingResult bindingResult, @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsService.save(goods,user);
        return ResultUtil.success();
    }

    @PostMapping("/assignGoods")
    public Result assignGoods(@RequestBody Goods goods, BindingResult bindingResult, @CurrentUser User user) {
        goodsService.assignGoods(goods,user);
        return ResultUtil.success();
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        goodsService.delete(id);
        return ResultUtil.success();
    }


    @GetMapping("/findAllGoodsDetail")
    @JsonConvert(
            type = GoodsDetail.class,
            includes = {"id", "goods", "stock"},
            nest = {
                    @NestConvert(type = Goods.class, includes = {"name"}),
            })
    public Page<GoodsDetail> findAllGoodsDetail(int page, int size, Goods goods){
        return goodsService.findAllGoodsDetail(page,size,goods);
    }

    @PostMapping("/updateStock")
    public Result updateStock(GoodsStockModel goodsStockModel,@CurrentUser User user){
        goodsService.updateStock(goodsStockModel,user);
        return ResultUtil.success();
    }

    @PostMapping("/addStock")
    public Result addStock(GoodsStockModel goodsStockModel, @CurrentUser User user){
        goodsService.addStock(goodsStockModel,user);
        return ResultUtil.success();
    }



}
