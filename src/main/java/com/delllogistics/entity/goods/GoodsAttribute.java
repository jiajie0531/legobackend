package com.delllogistics.entity.goods;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysGoodsCategory;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 *  商品规格
 * Created by calvin  2017/12/27
 */
@SQLDelete(sql = "update goods_attribute set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class GoodsAttribute extends BaseModel {


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
     * 关联企业
     */
    @NotNull(message = "关联企业不能为空")
    @Valid
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;

    /**
     * 属性分组
     */
    @OneToOne(fetch = FetchType.LAZY)
    private SysGoodsCategory sysGoodsCategory;


    /**
     * 属性明细
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="goods_attribute_id")
    @JSONField(serialize = false)
    private Set<GoodsAttributeDetail> goodsAttributeDetail;




}
