package com.delllogistics.entity.goods;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Tag about goods
 * @author JiaJie
 * @create 2017-11-05 1:04 PM
 **/
@SQLDelete(sql = "update goods_tag set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class GoodsTag extends BaseModel {
    /**
     * 标签名称
     */
    @NotNull(message = "标签名称不能为空")
    @Size(min = 1, max = 30, message = "标签名称长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 前端图标
     */
    @NotNull(message = "图标不能为空")
    @Size(min = 1, max = 30, message = "图标名称长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String icon;

}
