package com.delllogistics.entity.logistics;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.sys.SysExpress;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *  物流管理-快递公司管理
 * Created by calvin  2017/12/18
 */
@SQLDelete(sql = "update logistics_express set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
@Table(name = "logistics_express", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sys_express_id", "company_id" })
})
public class LogisticsExpress extends BaseModel {



    /**
     * 快递公司
     */
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SysExpress sysExpress;

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

    /**
     * 关联企业
     */
    @NotNull(message = "关联企业不能为空")
    @Valid
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JSONField(serialize = false)
    private Company company;


}
