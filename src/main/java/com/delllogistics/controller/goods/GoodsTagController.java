package com.delllogistics.controller.goods;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.goods.GoodsTag;
import com.delllogistics.service.goods.GoodsTagService;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/goodsTag")
public class GoodsTagController {

    private final GoodsTagService goodsTagService;

    @Autowired
    public GoodsTagController(GoodsTagService goodsTagService) {
        this.goodsTagService = goodsTagService;
    }

    @PostMapping("/findAll")
    public Page<GoodsTag> findAll(int page, int size, GoodsTag goodsTag) {
        return goodsTagService.findAll(page, size, goodsTag);
    }
    @PostMapping("/findAllSelect")
    public Iterable<GoodsTag> findAllSelect(Long companyId){
        return goodsTagService.findAllSelect(companyId);
    }

    @PostMapping("/save")
    public Result save(@Valid GoodsTag goodsBrand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsTagService.save(goodsBrand);
        return ResultUtil.success();
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        goodsTagService.delete(id);
        return ResultUtil.success();
    }
}
