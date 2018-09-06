package com.delllogistics.controller.goods;

import com.delllogistics.dto.Result;
import com.delllogistics.dto.goods.GoodsParamSearch;
import com.delllogistics.entity.goods.GoodsParam;
import com.delllogistics.entity.goods.GoodsParamValue;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.goods.GoodsParamService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestJsonController
@RequestMapping("/goodsParam")
public class GoodsParamController {

    private  final GoodsParamService goodsParamService;

    public GoodsParamController(GoodsParamService goodsParamService) {
        this.goodsParamService = goodsParamService;
    }

    /**
     * 查询商品参数
     * @param goodsParamSearch 参数搜索信息
     * @return 商品参数列表
     */
    @PostMapping("/findAll")
    @JsonConvert(
            type = GoodsParam.class,
            includes = {"id","name","goodsParamType","goodsParamValues","user","isUsed" ,"createTime"},
            nest = {
                    @NestConvert(type = GoodsParamValue.class, includes = {"id", "value","createUser","createTime"}),
                    @NestConvert(type = User.class, includes = {"id", "username"})
            })
    public Result findAll(@RequestBody GoodsParamSearch goodsParamSearch) {
        Page<GoodsParam> goodsParams = goodsParamService.findAll(goodsParamSearch);
        return ResultUtil.success(goodsParams);
    }

    /**
     * 查询商品参数列表
     * @param goodsParamSearch 参数搜索信息
     * @return 商品参数列表
     */
    @PostMapping("/findSelectAll")
    @JsonConvert(
            type = GoodsParam.class,
            includes = {"id","name","goodsParamType","goodsParamValues"},
            nest = {
                    @NestConvert(type = GoodsParamValue.class, includes = {"id", "value"}),
            }
     )
    public Result findSelectAll(@RequestBody GoodsParamSearch goodsParamSearch) {
        Page<GoodsParam> goodsParams = goodsParamService.findAll(goodsParamSearch);
        return ResultUtil.success(goodsParams);
    }

    /**
     * 商品参数编辑
     * @param goodsParam 商品参数
     * @param bindingResult 绑定结果
     * @return 返回结果
     */
    @PostMapping("/save")
    public Result save(@RequestBody GoodsParam goodsParam, BindingResult bindingResult, @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsParamService.save(goodsParam,user);
        return ResultUtil.success();
    }

    /**
     * 修改使用状态
     * @param goodsParam 商品参数
     * @param bindingResult 绑定结果
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/saveUsed")
    public Result saveUsed(@RequestBody GoodsParam goodsParam,BindingResult bindingResult, @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsParamService.saveUsed(goodsParam,user);
        return ResultUtil.success();
    }

    /**
     * 删除商品参数
     * @param goodsParam 商品参数
     * @param bindingResult 绑定结果
     * @param user 用户
     * @return 返回结果
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody GoodsParam goodsParam,BindingResult bindingResult,  @CurrentUser User user) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsParamService.delete(goodsParam,user);
        return ResultUtil.success();
    }

}
