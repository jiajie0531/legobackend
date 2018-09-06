package com.delllogistics.entity.order;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.enums.OrderServiceEvaluateStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 *  订单服务评价
 * Created by calvin  2018/1/23
 */
@Entity
@Getter
@Setter
@Table(name = "order_service_evaluate", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "order_main_id" })
})
public class OrderServiceEvaluate extends BaseModel {

    /**
     * 评论内容
     */
    private String content;
    /**
     * 快递包装评分
     */
    @NotNull(message = "快递包装评分不能为空")
    @Column(nullable = false)
    private int expressScore;
    /**
     * 配送速度评分
     */
    @NotNull(message = "配送速度评分不能为空")
    @Column(nullable = false)
    private int deliverspeedScore;
    /**
     * 配送员服务评分
     */
    @NotNull(message = "配送员服务评分不能为空")
    @Column(nullable = false)
    private int diliverymanScore;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32,nullable = false)
    private OrderServiceEvaluateStatus evaluateStatus;

    /**
     * 所属订单
     */
    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(nullable = false)
    private OrderMain orderMain;

    /**
     * 所属机构
     */
    @OneToOne(fetch =FetchType.LAZY )
    @JoinColumn
    private Company company;

    /**
     * 用户ID
     */
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;



}
