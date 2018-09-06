package com.delllogistics.entity.order;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.*;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

/**
 * 退款单
 * Created by calvin  2018/2/27
 */
@Entity
@Getter
@Setter
@Table(name = "order_refund", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"order_item_id","orderRefundStatus"})
})
public class OrderRefund extends BaseModel {
    /**
     * 关联订单明细
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id",nullable = false)
    @Valid
    private OrderItem orderItem;

    /**
     * 关联订单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_main_id")
    @Valid
    private OrderMain orderMain;


    /**
     * 退单编号
     */
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    /**
     * 服务类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private OrderRefundType orderRefundType;

    /**
     * 收货状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private LogisticsStatus logisticsStatus = LogisticsStatus.SENT;

    /**
     * 退款原因
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private OrderRefundReason orderRefundReason=OrderRefundReason.OTHER;

    /**
     * 退款金额
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal refundAmount=BigDecimal.ZERO;

    /**
     * 退款说明
     */
    //@Size(min = 1, max = 200, message = "备注长度必须在1和200之间")
    @Column(length = 200)
    private String refundDescText;

    /**
     * 是否扣款
     */
    private boolean isCharge;
    public void setIsCharge(boolean isCharge) {
        this.isCharge = isCharge;
    }
    public boolean getIsCharge() {
        return isCharge;
    }






    /**
     * 扣款金额
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal chargeAmount=BigDecimal.ZERO;

    /**
     * 扣款类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private OrderRefundChargeType chargeType;

    /**
     * 扣款说明
     */
    @Column(length = 200)
    private String chargeDescription;



    /**
     * 退款详细图片
     */
    @OneToMany
            @JoinTable(
            name = "OrderRefundPicsRelation",
            joinColumns = @JoinColumn(name = "order_refund_id"),
            inverseJoinColumns = @JoinColumn(name = "sysFile_id")
    )
    private Set<SysFile> detailPics;

    /**
     * 退款状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private OrderRefundStatus orderRefundStatus;


    /**
     * user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    /**
     * 所属机构
     */
    @ManyToOne(fetch =FetchType.LAZY )
    @JoinColumn
    private Company company;

    /**
     * 退货物流信息
     */
    @OneToOne(fetch = FetchType.LAZY,cascade={CascadeType.PERSIST})
    @JoinColumn
    private OrderRefundLogistics orderRefundLogistics;

}
