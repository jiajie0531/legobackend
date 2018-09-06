package com.delllogistics.entity.sys;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *  系统商品分类
 * Created by calvin  2017/12/27
 */
@SQLDelete(sql = "update sys_goods_category set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class SysGoodsCategory extends BaseModel {
    /**
     * 名称
     * 名称不能为空，而且长度必须在1和30之间
     */
    @NotNull(message = "名称不能为空")
    @Size(min = 1, max = 30, message = "名称长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String name;
    /**
     * 排序
     */
    @OrderBy
    @Column(nullable = false, columnDefinition = "int default 0")
    private int sort;








}
