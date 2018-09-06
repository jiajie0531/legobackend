package com.delllogistics.entity.goods;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.logistics.LogisticsTemplate;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysFile;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

/**
 * 商品信息
 * Created by jiajie on 23/10/2017.
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update goods set is_deleted=1,update_time=now() where id=? and version_=?")
public class Goods extends BaseModel {

    /**
     * 商品名称
     */
    @NotNull(message = "商品名称不能为空")
    @Size(min = 10, max = 60, message = "商品名称长度必须在10和60之间")
    @Column(nullable = false)
    private String name;

    /**
     * 商品编号
     */
    @Column(nullable = false)
    private String code;

    /**
     * 商品系列
     */
    @ManyToOne(fetch =FetchType.LAZY )
    @JoinColumn
    private GoodsSeries goodsSeries;


    /**
     * 标签货号
     */
    @Column(nullable = false)
    private String tagNo;

    /**
     * 热度
     */

    @Column(columnDefinition = "tinyint(1) default 0")
    private int hotValue;

    /**
     * 销量
     */
    @Column(columnDefinition = "tinyint(1) default 0")
    private int saleValue;


    /**
     * 描述
     */
    @NotNull(message = "商品描述不能为空")
    @Size(min = 1, max = 1000, message = "商品描述长度必须在1和1000之间")
    private String description;

    /**
     * 租赁价格；单位是 RMB／天；
     */
    @NotNull(message = "租赁价格不能为空")
    @DecimalMin(value = "0.00",message = "租赁价格必须大于0")
    @Column(nullable = false)
    private BigDecimal rentalPrice;

    /**
     * 押金；单位是 RMB／天；
     */
    @NotNull(message = "押金价格不能为空")
    @DecimalMin(value = "0.00",message = "押金价格必须大于0")
    @Column(nullable = false)
    private BigDecimal ensurePrice;


    /**
     * 品牌
     */
    @ManyToOne(fetch =FetchType.LAZY )
    @JoinColumn(nullable = false)
    private GoodsBrand goodsBrand;

    /**
     * 商品物流模板
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private LogisticsTemplate logisticsTemplate;

    /**
     * 库存数量
     */
    @DecimalMin(value = "0.00",message = "库存数量必须大于0")
    @Column(nullable = false)
    private BigDecimal stock;

    /**
     * 商品照片
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SysFile goodsPic;

    /**
     * 详情照片
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "GoodsDetailsPicsRelation",
            joinColumns = @JoinColumn(name = "goods_id"),
            inverseJoinColumns = @JoinColumn(name = "sysFile_id")
    )
    private Set<SysFile> detailPics;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "GoodTagRelation",
            joinColumns = @JoinColumn(name = "goods_id"),
            inverseJoinColumns = @JoinColumn(name = "goods_tag_id")
    )
    private Set<GoodsTag> goodsTags;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="goods_id")
    private Set<GoodsDetail> goodsDetail;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="goods_id")
    private Set<GoodsParamItem> goodsParamItems;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="goods_id")
    private Set<GoodsAttributeDetail> goodsAttributeDetail;





}
