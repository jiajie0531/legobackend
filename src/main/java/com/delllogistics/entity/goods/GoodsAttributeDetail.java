package com.delllogistics.entity.goods;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.SysFile;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *  商品规格详细
 * Created by calvin  2017/12/27
 */
@SQLDelete(sql = "update goods_attribute_detail set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class GoodsAttributeDetail extends BaseModel {

    /**
     * 名称
     * 名称不能为空，而且长度必须在1和30之间
     */
    @NotNull(message = "名称不能为空")
    @Size(min = 1, max = 30, message = "名称长度必须在1和30之间")
    @Column(nullable = false)
    private String name;

    /**
     * 排序
     */
    @OrderBy
    @Column(nullable = false, columnDefinition = "int default 0")
    private int sort;


    /**
     * 属性
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="goods_attribute_id")
    private GoodsAttribute goodsAttribute;


    /**
     * 商品详细
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="goods_detail_id",nullable = false)
    private GoodsDetail goodsDetail;

    /**
     * 商品
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id",nullable = false)
    private Goods goods;



    /**
     * 属性图片
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SysFile pic;

}
