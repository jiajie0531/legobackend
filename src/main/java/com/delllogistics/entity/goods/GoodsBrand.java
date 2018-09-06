package com.delllogistics.entity.goods;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.SysFile;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 商品品牌
 * Created by jiajie on 23/10/2017.
 */
@SQLDelete(sql = "update goods_brand set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class GoodsBrand extends BaseModel {
    /**
     * 品牌名称
     * 品牌名称不能为空，而且长度必须在1和30之间
     */
    @NotNull(message = "品牌名称不能为空")
    @Size(min = 1, max = 30, message = "品牌名称长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 品牌照片；
     */
    @OneToOne(fetch = FetchType.LAZY )
    @JoinColumn
    @Valid
    private SysFile descriptionPic;



}
