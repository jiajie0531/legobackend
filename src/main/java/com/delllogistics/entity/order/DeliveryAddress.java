package com.delllogistics.entity.order;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.sys.SysArea;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *  配送地址
 * Created by calvin  2017/12/18
 */
@SQLDelete(sql = "update delivery_address set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class DeliveryAddress extends BaseModel {

    /**
     * 收货人
     */
    @NotNull(message = "收货人不能为空")
    @Size(min = 1, max = 20, message = "收货人长度必须在1和20之间")
    @Column(nullable = false)
    private String consignee;

    /**
     * 所在地区；
     */
    @JoinColumn(name = "area_id",nullable = false)
    @ManyToOne(fetch= FetchType.LAZY)
    private SysArea area;

    /**
     * 详细地址
     */
    @Size(min = 5, max = 100, message = "详细地址长度必须在5和100之间")
    @Column(length = 100,nullable = false)
    private String detailedAddress;

    /**
     * 手机号码
     */
    @NotNull(message = "手机号码不能为空")
    @Size(min = 13, max = 13, message = "手机号码长度为11位")
    @JoinColumn(nullable = false)
    private String phone;

    /**
     * 用户
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;






}
