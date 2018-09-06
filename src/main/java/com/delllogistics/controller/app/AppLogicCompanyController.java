package com.delllogistics.controller.app;

import com.delllogistics.dto.BaseSelect;
import com.delllogistics.service.app.AppLogisticsExpressService;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 *  订单详细评价
 * Created by calvin  2018/1/23
 */
@RestJsonController
@RequestMapping("/app/logisticsExpress")
public class AppLogicCompanyController {

    private final AppLogisticsExpressService appLogisticsExpressService;

    public AppLogicCompanyController(AppLogisticsExpressService appLogisticsExpressService) {
        this.appLogisticsExpressService = appLogisticsExpressService;
    }

    /**
     * 查询快递列表
     * @param companyId 企业id
     * @return 返回结果集
     */
    @GetMapping("/findAllSelect")
    @JsonConvert(
            type = BaseSelect.class,
            includes = {"id", "name"}
    )
    public List findAllSelect(Long companyId) {
        return appLogisticsExpressService.findAllSelect(companyId);
    }



}
