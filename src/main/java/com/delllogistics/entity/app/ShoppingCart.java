package com.delllogistics.entity.app;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsDetail;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Shopping Cart.<br/>
 * User: jiajie<br/>
 * Date: 19/11/2017<br/>
 * Time: 9:02 PM<br/>
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update shopping_cart set is_deleted = 1, update_time = now() where id=? and version_=?")
public class ShoppingCart extends BaseModel {

    /**
     * goods id
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    private Goods goods;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_detail_id")
    @Valid
    private GoodsDetail goodsDetail;

    
    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    @DecimalMin(value = "0.00", message = "购买数量必须大于0")
    private BigDecimal quantity;

    /**
     * user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;
}
