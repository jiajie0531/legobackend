package com.delllogistics.entity.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

/**
 * 企业信息
 * Created by jiajie on 14/10/2017.
 */
@SQLDelete(sql = "update company set is_deleted=1,update_time=now() where id=? and version_=?")
@Entity
@Getter
@Setter
public class Company extends BaseModel {
    @Column(nullable = false)
    private String name;
    private String address;
    private String contactUser;
    private String contactPhone;

    @ManyToOne
    @JoinColumn(name="parent_id")
    @JSONField(serialize = false)
    private Company parent;

    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    @OrderBy("id")
    @Where(clause = "is_deleted=0")
    private Set<Company> children;

    @ManyToOne
    @JoinColumn
    @JSONField(serialize = false)
    private Company createCompany;
}
