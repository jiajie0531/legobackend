package com.delllogistics.controller.goods;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.goods.GoodsBrand;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.service.goods.GoodsBrandService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.LogAnnotation;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Created by guangxingju on 2017/11/2.
 */
@RestJsonController
@RequestMapping("/goodsBrand")
public class GoodsBrandController {
    private final GoodsBrandService goodsBrandService;

    @Autowired
    public GoodsBrandController(GoodsBrandService goodsBrandService) {
        this.goodsBrandService = goodsBrandService;
    }

    /**
     * 查询品牌信息
     * @param page 页数
     * @param size 大小
     * @param goodsBrand 品牌
     * @return 返回结果
     */
    @PostMapping("/findAll")
    @LogAnnotation(value="删除品牌信息")
    @JsonConvert(
            type = GoodsBrand.class,
            includes = {"id", "name","descriptionPic","createTime"},
            nest = {
                    @NestConvert(type = SysFile.class, includes = {"id","uid","url"})
            }
    )
    public Page<GoodsBrand> findAll(int page, int size, GoodsBrand goodsBrand) {
        return goodsBrandService.findAll(page, size, goodsBrand);
    }

    @PostMapping("/findAllSelect")
    @JsonConvert(
            type = GoodsBrand.class,
            includes = {"id", "name"}
    )
    public Iterable<GoodsBrand> findAllSelect(){
        return goodsBrandService.findAllSelect();
    }

    /**
     * 保存品牌信息
     * @param goodsBrand 品牌
     * @param bindingResult 绑定结果
     * @return 返回结果
     */
    @PostMapping("/save")
    @LogAnnotation(value="保存品牌信息")
    public Result save(@Valid GoodsBrand goodsBrand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        goodsBrandService.save(goodsBrand);
        return ResultUtil.success();
    }

    /**
     * 删除品牌信息
     * @param id 品牌id
     * @return 返回结果
     */
    @PostMapping("/delete")
    @LogAnnotation(value="删除品牌信息")
    public Result delete(Long id) {
        goodsBrandService.delete(id);
        return ResultUtil.success();
    }

}
