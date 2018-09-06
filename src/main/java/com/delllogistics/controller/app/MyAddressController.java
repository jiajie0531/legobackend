package com.delllogistics.controller.app;

import com.delllogistics.dto.Result;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.app.UserAddress;
import com.delllogistics.service.app.UserAddressService;
import com.delllogistics.spring.annotation.CurrentUser;
import com.delllogistics.spring.annotation.JsonConvert;
import com.delllogistics.spring.annotation.NestConvert;
import com.delllogistics.spring.annotation.RestJsonController;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RestJsonController
@RequestMapping("app/myAddress")
public class MyAddressController {
    private final UserAddressService userAddressService;

    @Autowired
    public MyAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @PostMapping("/save")
    public Result save(@Valid UserAddress userAddress,Long areaId, @CurrentUser User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(-1, bindingResult.getFieldError().getDefaultMessage());
        }
        return userAddressService.save(userAddress,areaId,user);
    }

    /**
     * 删除收货地址
     * @param id
     * @return
     */
    @PostMapping("/deleteAddress")
    public Result deleteAddress(Long id) {
         userAddressService.deleteAddress(id);
        return ResultUtil.success();
    }

    /**
     * 获取所有收货地址
     * @param user
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/findAllAddressList")
    @JsonConvert(
            type = UserAddress.class,
            includes = {"id","consignee","phone","detailedAddress","isUsed","area"},
            nest = {
                    @NestConvert(type=SysArea.class,includes = {"id","name","level","sort","parent",})
            })
    public Page<UserAddress> findAllAddressList(@CurrentUser User user, int page, int size) {
        return userAddressService.findAllAddressList(page,size,user);
    }

    /**
     * 获取可使用收货地址
     * @param id
     * @param user
     * @return
     */
    @PostMapping("/isUsed")
    public Result isUsed(Long id, @CurrentUser User user) {
        if (id==0) {
            return ResultUtil.error(-1, "数据格式有误!");
        }
        return userAddressService.isUsed(user.getId(),id);
    }

    /**
     * 获取默认收货地址
     * @param user
     * @return
     */
    @GetMapping("/findDefaultAddress")
    @JsonConvert(
            type = UserAddress.class,
            includes = {"id","consignee","phone","detailedAddress","area"},
            nest = {
                    @NestConvert(type=SysArea.class,includes = {"id","name","level","sort","parent",})
            })
    public Result findDefaultAddress(@CurrentUser User user){
        UserAddress userAddress=userAddressService.findDefaultAddress(user);
        return ResultUtil.success(userAddress);
    }
}