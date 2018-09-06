package com.delllogistics.entity.order;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.enums.OrderLogisticsType;
import com.delllogistics.entity.enums.OrderMainStatus;
import com.delllogistics.entity.enums.OrderServiceEvaluateStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Order Main.<br/>
 * User: jiajie<br/>
 * Date: 16/11/2017<br/>
 * Time: 8:58 PM<br/>
 */
@Entity
@Getter
@Setter
@Table(name = "order_main", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code"})
})
public class OrderMain extends BaseModel {

    /**
     * 订单编号
     */
    @Column(nullable = false, unique = true, length = 32)
    private String code;

    /**
     * 备注
     */
    @Size(min = 1, max = 200, message = "备注长度必须在1和200之间")
    @Column(length = 200)
    private String remarks;

    /**
     * 配送费用
     */
    @NotNull(message = "配送费用不能为空")
    @Column(precision = 12, scale = 2)
    private BigDecimal deliveryFee;

    /**
     * 应付款金额
     */
    @NotNull(message = "应付款金额不能为空")
    @DecimalMin(value = "0.00", message = "应付款金额必须大于0")
    @Column(precision = 12, scale = 2)
    private BigDecimal orderAmount;

    /**
     * 押金合计
     */
    @NotNull(message = "押金合计不能为空")
    @DecimalMin(value = "0.00", message = "押金合计必须大于0")
    @Column(precision = 12, scale = 2)
    private BigDecimal ensureAmount;
    /**
     * 租金合计
     */
    @NotNull(message = "租金合计不能为空")
    @DecimalMin(value = "0.00", message = "租金合计必须大于0")
    @Column(precision = 12, scale = 2)
    private BigDecimal rentalAmount;

    /**
     * 商品的总金额
     */
    @NotNull(message = "商品的总金额不能为空")
    @Column(precision = 12, scale = 2)
    private BigDecimal goodsAmount;
    /**
     * 优惠金额
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal discountAmount;
    /**
     * 收货人
     */
    @NotNull(message = "收货人不能为空")
    @Size(min = 1, max = 20, message = "收货人长度必须在1和20之间")
    @Column(length = 100,nullable = false)
    private String consignee;

    /**
     * 所在地区；
     */
    @JoinColumn(name = "area_id",nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private SysArea area;
    /**
     * 详细地址
     */
    @NotNull(message = "详细地址不能为空")
    @Size(min = 5, max = 100, message = "详细地址长度必须在5和100之间")
    @Column(length = 100,nullable = false)
    private String detailedAddress;

    /**
     * 手机号码
     */
    @NotNull(message = "手机号码不能为空")
    @Size(min = 13, max = 13, message = "手机号码长度为11位")
    @Column(length = 32,nullable = false)
    private String phone;

    /**
     * 交付时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryTime;

    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private OrderMainStatus status;

    /**
     * 订单服务评价
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private OrderServiceEvaluateStatus orderServiceEvaluateStatus;

    /**
     * 配送类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32, columnDefinition = "varchar(32) default 'EMPLOYEE'")
    private OrderLogisticsType orderLogisticsType = OrderLogisticsType.EMPLOYEE;

    /**
     * 配送日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;

    /**
     * 所属机构
     */
    @ManyToOne(fetch =FetchType.LAZY )
    @JoinColumn
    private Company company;

    /**
     * user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @NotNull(message = "租用天数不能为空")
    @Min(value = 1, message = "租用天数必须大于0")
    private int dayNum;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="order_main_id")
    private List<OrderItem>  orderItems;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="order_main_id")
    private List<OrderActionLog>  orderActionLogs;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="order_main_id")
    private List<OrderRefund> orderRefunds;

}
