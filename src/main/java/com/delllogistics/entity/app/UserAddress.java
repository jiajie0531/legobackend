package com.delllogistics.entity.app;

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
 *  收货地址管理
 * Created by calvin  2017/12/9
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update user_address set is_deleted=1,update_time=now() where id=? and version_=?")
public class UserAddress extends BaseModel {
    /**
     * 收货人
     */
    @NotNull(message = "收货人不能为空")
    @Size(min = 1, max = 20, message = "收货人长度必须在1和20之间")
    @Column(nullable = false)
    private String consignee;

    /**
     * 收货人
     */
    @JoinColumn(name = "user_id",nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private User user;


    /**
     * 详细地址
     */
    @NotNull(message = "详细地址不能为空")
    @Size(min = 5, max = 100, message = "详细地址长度必须在5和100之间")
    @Column(nullable = false)
    private String detailedAddress;

    /**
     * 手机号码
     */
    @NotNull(message = "手机号码不能为空")
    @Size(min = 13, max = 13, message = "手机号码长度为11位")
    @Column(nullable = false)
    private String phone;
    /**
     * 所在地区；
     */
    @JoinColumn(name = "area_id",nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private SysArea area;
    /**
     * 是否默认
     */
    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean isUsed;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }

}
