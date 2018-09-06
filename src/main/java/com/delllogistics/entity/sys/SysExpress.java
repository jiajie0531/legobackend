package com.delllogistics.entity.sys;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *  快递公司
 * Created by calvin  2017/12/18
 */
@SQLDelete(sql = "update sys_express set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
@Table(name = "sys_express", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "code", "secret"})
})
public class SysExpress extends BaseModel {
    /**
     * 快递公司名称
     * 快递公司名称不能为空，而且长度必须在1和30之间
     */
    @NotNull(message = "快递公司名称不能为空")
    @Size(min = 1, max = 30, message = "快递名称长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 快递公司代码
     * 快递公司代码不能为空，而且长度必须在1和30之间
     */
    @NotNull(message = "快递公司代码不能为空")
    @Size(min = 1, max = 30, message = "快递公司代码长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String code;


    /**
     * 接口secret码
     * 接口secret码不能为空，而且长度必须在1和30之间
     */
    @NotNull(message = "接口secret码不能为空")
    @Size(min = 1, max = 30, message = "接口secret码长度必须在1和30之间")
    @Column(nullable = false, unique = true)
    private String secret;


    /**
     * 是否可用
     */
    @NotNull(message = "是否可用不能为空")
    private boolean isUsed;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }



}
