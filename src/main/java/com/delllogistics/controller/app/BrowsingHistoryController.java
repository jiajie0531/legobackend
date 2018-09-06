package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.app.BrowsingHistory;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsSeries;
import com.delllogistics.entity.goods.GoodsTag;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.app.BrowsingHistoryService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 足迹.<br/>
 * User: jiajie<br/>
 * Date: 09/12/2017<br/>
 * Time: 10:46 PM<br/>
 */
@RestJsonController
@RequestMapping("/app/browsingHistory")
public class BrowsingHistoryController {

    private final BrowsingHistoryService browsingHistoryService;

    @Autowired
    public BrowsingHistoryController(BrowsingHistoryService browsingHistoryService) {
        this.browsingHistoryService = browsingHistoryService;
    }


    /**
     * 添加足迹
     * @param user      用户
     * @param goodsId   商品id
     * @return  Result
     */
    @PostMapping("/addBrowsingHistory")
    public Result addBrowsingHistory(@CurrentUser User user, long goodsId) {
        browsingHistoryService.addBrowsingHistory(user, goodsId);
        return ResultUtil.success();
    }
    /**
     * 添加足迹
     * @param user      用户
     * @return  Result
     */
    @GetMapping("/findBrowsingHistoryCountByUser")
    public Result findBrowsingHistoryCountByUser(@CurrentUser User user,@RequestParam  Long companyId) {
        return ResultUtil.success(browsingHistoryService.findBrowsingHistoryCountByUser(user,companyId));
    }

    /**
     * 显示足迹列表
     * @param user  用户
     * @return  Result
     */
    @GetMapping("/findBrowsingHistory")
    @JsonConvert(
            type = BrowsingHistory.class,
            includes = {"goods"},
            nest = {
                    @NestConvert(type = Goods.class, includes = {"id", "name", "rentalPrice", "ensurePrice", "goodsPic", "goodsTags", "goodsSeries"}),
                    @NestConvert(type = GoodsTag.class, includes = {"name", "icon"}),
                    @NestConvert(type = GoodsSeries.class, includes = {"name"}),
                    @NestConvert(type = SysFile.class, includes = "url"),
            })
    public Page<BrowsingHistory> findBrowsingHistory(int page, int size, @CurrentUser User user,@RequestParam  Long companyId) {
        return browsingHistoryService.findBrowsingHistory(page, size, user,companyId);
    }
}
