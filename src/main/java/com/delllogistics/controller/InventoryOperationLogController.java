package com.delllogistics.controller;

import com.delllogistics.dto.BaseSearchModel;
import com.delllogistics.entity.goods.GoodsDetail;
import com.delllogistics.entity.goods.InventoryOperationLog;
import com.delllogistics.entity.user.User;
import com.delllogistics.service.InventoryOperationLogService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 库存操作记录表.<br/>
 * User: jiajie<br/>
 * Date: 17/03/2018<br/>
 * Time: 11:55 AM<br/>
 */
@RestJsonController
@RequestMapping("/inventoryOperationLog")
public class InventoryOperationLogController {

    private final InventoryOperationLogService inventoryOperationLogService;

    @Autowired
    public InventoryOperationLogController(InventoryOperationLogService inventoryOperationLogService) {
        this.inventoryOperationLogService = inventoryOperationLogService;
    }

    @GetMapping("/findAllStockLog")
    @JsonConvert(
            type = InventoryOperationLog.class,
            includes = {"id","initQuantity", "operateQuantity", "quantity","operate","code","createTime","createUser"},
            nest = {@NestConvert(type = User.class,includes = {"username"})}
            )
    public Page<InventoryOperationLog> findAllStockLog(BaseSearchModel baseSearchModel, GoodsDetail goodsDetail){
        return inventoryOperationLogService.findAllStockLog(baseSearchModel,goodsDetail);
    }
}
