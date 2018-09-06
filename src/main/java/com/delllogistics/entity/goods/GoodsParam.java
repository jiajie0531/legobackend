package com.delllogistics.entity.goods;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.SysGoodsCategory;
import com.delllogistics.entity.enums.GoodsParamType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 *  商品参数表
 * Created by calvin  2017/12/27
 */
@SQLDelete(sql = "update goods_param set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class GoodsParam extends BaseModel {
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
     * 商品参数类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10,nullable = false)
    @NotNull(message = "商品参数类型不能为空")
    private GoodsParamType goodsParamType;


    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="goods_param_id")
    private Set<GoodsParamValue> goodsParamValues;


    /**
     * 系统商品分类
     */
    @OneToOne(fetch = FetchType.LAZY)
    private SysGoodsCategory sysGoodsCategory;

    /**
     * 是否可用
     */
    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean isUsed;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }





}
