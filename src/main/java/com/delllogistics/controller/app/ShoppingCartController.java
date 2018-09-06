package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.app.ShoppingCart;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsDetail;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.ShoppingCartService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author JiaJie
 * 2017-11-24 4:55 PM
 **/
@RestJsonController
@RequestMapping("/app/shoppingCart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    /**
     * 获取购物车列表
     * @param user
     * @return
     */
    @GetMapping("/findShoppingCart")
    @JsonConvert(
            type = ShoppingCart.class,
            includes = {"id","goods","quantity", "goodsDetail"},
            nest = {
                    @NestConvert(type=Goods.class,includes = {"id","goodsPic","name","ensurePrice","rentalPrice"}),
                    @NestConvert(type =SysFile.class,includes = {"url"}),
                    @NestConvert(type = GoodsDetail.class, includes = {"id","stock","rentalPrice","ensurePrice"}),
            })
    public List<ShoppingCart> findShoppingCart(@CurrentUser User user,@RequestParam Long companyId) {
        return shoppingCartService.findShoppingCart(user,companyId);
    }

    /**
     * 添加购物车
     * @param shoppingCart
     * @param user
     * @return
     */
    @PostMapping("/addToShoppingCart")
    public Result addToShoppingCart(@Valid ShoppingCart shoppingCart, @CurrentUser User user)  {
        shoppingCartService.addToShoppingCart(shoppingCart,user);
        return ResultUtil.success();
    }

    /**
     * 更新购物车
     * @param shoppingCarts
     * @return
     */
    @PostMapping("/updateShoppingCart")
    public Result updateShoppingCart(@RequestBody List<ShoppingCart> shoppingCarts){
        shoppingCartService.updateShoppingCart(shoppingCarts);
        return ResultUtil.success();
    }

    /**
     * 删除购物车商品
     * @param id
     * @return
     */
    @PostMapping("/remove")
    public Result remove(Long id){
        shoppingCartService.remove(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除购物车商品
     * @param ids
     * @return
     */
    @PostMapping("/removeByIds")
    public Result removeByIds(Long[] ids) {
        shoppingCartService.removeByIds(ids);
        return ResultUtil.success();
    }

    /**
     * 清空购物车
     * @param user
     * @return
     */
    @PostMapping("/removeAll")
    public Result removeAll(@CurrentUser User user){
        shoppingCartService.removeAll(user.getId());
        return ResultUtil.success();
    }

    /**
     * 获取购物车数量
     * @param user
     * @param goods
     * @return
     */
    @PostMapping("/findShoppingCartCount")
    public Result findShoppingCartCount(@CurrentUser User user,Goods goods ){
        int count= shoppingCartService.findShoppingCartCount(user,goods);
        return ResultUtil.success(count);
    }
}
