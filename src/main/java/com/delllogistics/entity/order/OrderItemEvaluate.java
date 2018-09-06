package com.delllogistics.entity.order;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.enums.OrderItemEvaluateStatus;
import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.sys.SysFile;
import com.delllogistics.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;


/**
 *  订单详细评价
 * Created by calvin  2018/1/23
 */
@Entity
@Getter
@Setter
@Table(name = "order_item_evaluate", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","order_item_id"})
})
public class OrderItemEvaluate extends BaseModel {

    /**
     * 评论内容
     */
    private String content;

    /**
     * 商品评分
     */
    @NotNull(message = "商品评分不能为空")
    @Column(nullable = false)
    private int goodsScore;


    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private OrderItemEvaluateStatus evaluateStatus;

    /**
     * 评论详细图片
     */
    @OneToMany
    @JoinTable(
            name = "OrderItemEvaluatePicsRelation",
            joinColumns = @JoinColumn(name = "orderItemEvaluate_id"),
            inverseJoinColumns = @JoinColumn(name = "sysFile_id")
    )
    private Set<SysFile> detailPics;

    /**
     * 所属商品
     */
    @NotNull(message = "商品不能为空")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private Goods goods;

    /**
     * 所属订单详细
     */
    @NotNull(message = "订单详细不能为空")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private OrderItem orderItem;

    /**
     * 用户ID
     */
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


}
