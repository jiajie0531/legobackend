package com.delllogistics.entity.order;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.OrderActionType;
import com.delllogistics.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 *  订单操作记录
 * Created by calvin  2018/1/15
 */
@Entity
@Getter
@Setter
public class OrderActionLog extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="order_main_id",nullable = false)
    @Valid
    private OrderMain orderMain;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="order_item_id")
    @Valid
    private OrderItem orderItem;

    /**
     * 操作人员
     */
    @ManyToOne
    @JoinColumn
    private User user;


    /**
     * 备注
     */
    @Size(min = 1, max = 200, message = "备注长度必须在1和200之间")
    @Column(length = 200)
    private String remarks;

    /**
     * 操作类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private OrderActionType orderActionType;

    /**
     * 操作状态
     */
    @Column(length = 32,nullable = false)
    private String status;

}
