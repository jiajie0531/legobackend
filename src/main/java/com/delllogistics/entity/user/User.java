package com.delllogistics.entity.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.Finance.FinanceDeposit;
import com.delllogistics.entity.enums.UserStatus;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.sys.SysResourceRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@SQLDelete(sql = "update user set is_deleted=1,update_time=now() where id=? and version_=?")
public class User extends BaseModel {

    @Column(name = "username", nullable = false, unique = true, columnDefinition = " VARCHAR(255) COMMENT '会员等级名称' ")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    private String password;

    private String token;

    /**
     * 用户类型
     */
    @Column(nullable = false)
    private int type;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private SysFile avatar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private SysFile background;

    private String mobile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private WechatUser wechatUser;

    /**
     * 微信union id
     */
    private String wechatUnionId;


    /**
     * 性别
     */
    private int gender;

    /**
     * 城市
     */
    private String city;

    /**
     * 描述
     */
    private String description;

    /**
     * 个人头像
     */
    private String photo;

    /**
     * 邀请人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User inviter;

    @OneToMany
    @JoinTable(
            name = "UserRole",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<SysResourceRole> roles;


    /**
     * 当前关联企业
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Company company;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "UserCompany",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private Set<Company> companies;

    /**
     * 用户状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private UserStatus userStatus = UserStatus.NORMAL;


}
