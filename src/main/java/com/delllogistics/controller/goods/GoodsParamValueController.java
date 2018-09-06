package com.delllogistics.controller.goods;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.goods.GoodsParamValueSearch;
import com.delllogistics.entity.goods.GoodsParamValue;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.goods.GoodsParamValueService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestJsonController
@RequestMapping("/goodsParamValue")
public class GoodsParamValueController {

    private  final GoodsParamValueService goodsParamValueService;

    public GoodsParamValueController(GoodsParamValueService goodsParamValueService) {
        this.goodsParamValueService = goodsParamValueService;
    }

    /**
     * 查询商品参数
     * @param goodsParamValueSearch 参数搜索信息
     * @return 商品参数列表
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = GoodsParamValue.class,
            includes = {"id","value" ,"createTime"}
            )
    public Result findAll(@RequestBody GoodsParamValueSearch goodsParamValueSearch) {
        Page<GoodsParamValue> goodsParamValues = goodsParamValueService.findAll(goodsParamValueSearch);
        return ResultUtil.success(goodsParamValues);
    }

    /**
     * 商品参数编辑
     * @param goodsParamValue
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody GoodsParamValue goodsParamValue, BindingResult bindingResult, @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsParamValueService.save(goodsParamValue,user);
        return ResultUtil.success();
    }

    /**
     * 删除商品参数
     * @param goodsParamValue
     * @param bindingResult
     * @param user
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody GoodsParamValue goodsParamValue,BindingResult bindingResult,  @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsParamValueService.delete(goodsParamValue,user);
        return ResultUtil.success();
    }

}
