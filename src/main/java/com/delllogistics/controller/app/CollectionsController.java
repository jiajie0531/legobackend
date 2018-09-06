package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.app.Collections;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsSeries;
import com.delllogistics.entity.goods.GoodsTag;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.exception.ExceptionCode;
import com.delllogistics.exception.ServiceException;
import com.delllogistics.service.app.CollectionsService;
import com.delllogistics.service.goods.GoodsService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 收藏.<br/>
 * User: jiajie<br/>
 * Date: 09/12/2017<br/>
 * Time: 10:44 PM<br/>
 */
@RestJsonController
@RequestMapping("/app/collections")
public class CollectionsController {

    private final CollectionsService collectionsService;
    private final GoodsService goodsService;

    @Autowired
    public CollectionsController(CollectionsService collectionsService, GoodsService goodsService) {
        this.collectionsService = collectionsService;
        this.goodsService = goodsService;
    }

    /**
     * 添加收藏
     *
     * @param user  用户
     * @param goods 商品id
     * @return Result
     */
    @PostMapping("/addCollection")
    public Result addCollection(@CurrentUser User user, Goods goods) {
        if (ObjectUtils.isEmpty(goods)) {
            throw new ServiceException(ExceptionCode.GOODS_DISABLE, "无效商品");
        }
        collectionsService.addCollection(user, goods);
        return ResultUtil.success();
    }

    /**
     * 取消收藏
     *
     * @param goods 商品
     * @return Result
     */
    @PostMapping("/deleteCollection")
    public Result deleteCollection(@CurrentUser User user, Goods goods) {
        if (ObjectUtils.isEmpty(goods.getId())) {
            return ResultUtil.error(-1, "无效商品");
        }
        if (collectionsService.findCollectionsCountByGoods(user, goods) < 1) {
            return ResultUtil.error(-1, "错误商品");
        }
        collectionsService.deleteCollection(user, goods);
        return ResultUtil.success();
    }

    /**
     * 根据商品查询收藏
     *
     * @param user    用户
     * @param goodsId 商品
     * @return Result
     */
    @GetMapping("/findCollectionByGoodsId")
    public Result findCollectionByGoodsId(@CurrentUser User user, long goodsId) {

        if (StringUtils.isEmpty(goodsId)) {
            return ResultUtil.error(-1, "无效商品");
        }
        Goods goods = goodsService.findById(goodsId);
        if (StringUtils.isEmpty(goods)) {
            return ResultUtil.error(-1, "无效商品");
        }
        Collections collections = collectionsService.findCollectionByGoodsId(user, goods);
        if (StringUtils.isEmpty(collections) || collections.getIsDeleted()) {
            return ResultUtil.error(-1, "商品暂未收藏");
        } else {
            return ResultUtil.success();
        }
    }

    /**
     * 显示收藏列表
     *
     * @param user 用户
     * @return Result
     */
    @GetMapping("/findCollections")
    @JsonConvert(
            type = Collections.class,
            includes = {"goods"},
            nest = {
                    @NestConvert(type = Goods.class, includes = {"id", "name", "rentalPrice", "ensurePrice", "goodsPic", "goodsTags", "goodsSeries"}),
                    @NestConvert(type = GoodsTag.class, includes = {"name", "icon"}),
                    @NestConvert(type = GoodsSeries.class, includes = {"name"}),
                    @NestConvert(type = SysFile.class, includes = "url"),
            })
    public Page<Collections> findGoodsBySeries(int page, int size, @CurrentUser User user,@RequestParam Long companyId) {
        return collectionsService.findCollections(page, size, user,companyId);
    }

    /**
     * 查询用户收藏数量
     *
     * @param user 用户
     * @return Result
     */
    @GetMapping("/findCollectionsCountByUser")
    public Result findCollectionsCountByUser(@CurrentUser User user,@RequestParam  Long companyId) {
        return ResultUtil.success(collectionsService.findCollectionsCountByUser(user,companyId));
    }

    /**
     * 查询商品收藏数量
     *
     * @param user 用户
     * @return Result
     */
    @GetMapping("/findCollectionsCountByGoods")
    public Result findCollectionsCountByGoods(@CurrentUser User user, long goodsId) {
        if (StringUtils.isEmpty(goodsId)) {
            return ResultUtil.error(-1, "无效商品");
        }
        Goods goods = goodsService.findById(goodsId);
        if (StringUtils.isEmpty(goods)) {
            return ResultUtil.error(-1, "无效商品");
        }
        return ResultUtil.success(collectionsService.findCollectionsCountByGoods(user, goods));
    }


}
