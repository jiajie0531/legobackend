package com.delllogistics.entity.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 *  会员等级
 * Created by calvin  2018/1/5
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update membership_rank set is_deleted=1,update_time=now() where id=? and version_=?")
public class MembershipRank extends BaseModel {

    /**
     * 会员等级名称
     */
    @NotNull(message = "会员等级名称不能为空")
    @Size(min = 1, max = 30, message = "会员等级名称长度必须在1和30之间" )
    @Column( nullable = false, unique = true, columnDefinition=" VARCHAR(255) COMMENT '会员等级名称'  ")
    private String name;

    /**
     * 会员等级图标
     */
    @NotNull(message = "会员等级图标不能为空")
    @Size(min = 1, max = 30, message = "会员等级图标长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String icon;
    /**
     * 该等级的最低积分
     */
    @NotNull(message = "该等级的最低积分不能为空")
    @Column(nullable = false)
    private int minPoints;

    /**
     * 该等级的最高积分
     */
    @NotNull(message = "该等级的最高积分不能为空")
    @Column(nullable = false)
    private int maxPoints;

    /**
     * 该会员等级的商品折扣
     */
    @NotNull(message = "会员等级的商品折扣不能为空")
    @DecimalMin(value = "0.00", message = "会员等级的商品折扣必须大于0")
    @Column(precision = 12, scale = 2)
    private BigDecimal discount;

    /**
     * 会员等级描述
     */
    private String description;


    /**
     * 关联企业
     */
    @NotNull(message = "关联企业不能为空")
    @Valid
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;


}
