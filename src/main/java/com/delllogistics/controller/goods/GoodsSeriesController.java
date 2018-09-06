package com.delllogistics.controller.goods;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.goods.GoodsSeries;
import com.delllogistics.entity.sys.SysExpress;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.service.goods.GoodsSeriesService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RestJsonController
@RequestMapping("/goodsSeries")
public class GoodsSeriesController {
    private final GoodsSeriesService goodsSeriesService;

    @Autowired
    public GoodsSeriesController(GoodsSeriesService goodsSeriesService) {
        this.goodsSeriesService = goodsSeriesService;
    }


    @PostMapping("/findAll")
    @JsonConvert(
            type = GoodsSeries.class,
            includes = {"id", "name","logoPic","createTime","descriptionPic","sort","enName","links","isUsed"}
    )
    public Page<GoodsSeries> findAll(int page, int size, GoodsSeries goodsSeries) {
        return goodsSeriesService.findAll(page, size, goodsSeries,false);
    }
    @JsonConvert(
            type = GoodsSeries.class,
            includes = {"id", "name"},
            nest = {
                 @NestConvert(type = SysFile.class, includes = {"url"})
            }
    )

    /**
     * 查询系列列表
     * @return 返回结果集
     */
    @PostMapping("/findAllSelect")
    public Iterable<GoodsSeries> findAllSelect(){
        return goodsSeriesService.findAllSelect();
    }





    @PostMapping("/save")
    public Result save(@Valid GoodsSeries goodsSeries, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsSeriesService.save(goodsSeries);
        return ResultUtil.success();
    }

    @PostMapping("/saveUsed")
    public Result saveUsed(GoodsSeries goodsSeries, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsSeriesService.saveUsed(goodsSeries);
        return ResultUtil.success();
    }



    @PostMapping("/delete")
    public Result delete(Long id) {
        goodsSeriesService.delete(id);
        return ResultUtil.success();
    }

}
