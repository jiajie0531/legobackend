package com.delllogistics.entity.order;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.OrderItemEvaluateStatus;
import com.delllogistics.entity.enums.OrderRefundStatus;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsDetail;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Order Item.<br/>
 * User: jiajie<br/>
 * Date: 16/11/2017<br/>
 * Time: 9:01 PM<br/>
 */
@Entity
@Getter
@Setter
public class OrderItem extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="order_main_id",nullable = false)
    @Valid
    private OrderMain orderMain;


    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST})
    @JoinColumn(name = "goods_detail_id",nullable = false)
    @Valid
    private GoodsDetail goodsDetail;

    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST})
    @JoinColumn(name = "goods_id",nullable = false)
    @Valid
    private Goods goods;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @DecimalMin(value = "0.00", message = "数量必须大于0")
    @Column(precision = 12, scale = 2,nullable = false)
    private BigDecimal quantity;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.00", message = "单价必须大于0")
    @Column(precision = 12, scale = 2,nullable = false)
    private BigDecimal price;


    /**
     * 租赁价格；单位是 RMB／天；
     */
    @NotNull(message = "租赁价格不能为空")
    @DecimalMin(value = "0.00",message = "租赁价格必须大于0")
    @Column(precision = 12, scale = 2,nullable = false)
    private BigDecimal rentalPrice;

    /**
     * 押金；单位是 RMB／天；
     */
    @NotNull(message = "押金价格不能为空")
    @DecimalMin(value = "0.00",message = "押金价格必须大于0")
    @Column(precision = 12, scale = 2,nullable = false)
    private BigDecimal ensurePrice;


    /**
     * 过期日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;


    /**
     * 订单详细评价状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private OrderItemEvaluateStatus orderItemEvaluateStatus;


    /**
     * 订单明细退款状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private OrderRefundStatus orderRefundStatus;



    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="order_item_id")
    private List<OrderRefund> orderRefunds;


}
