package com.delllogistics.entity.app;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.goods.Goods;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

/**
 * 收藏.<br/>
 * User: jiajie<br/>
 * Date: 09/12/2017<br/>
 * Time: 10:05 PM<br/>
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update collections set is_deleted=1,update_time=now() where id=? and version_=?")
public class Collections extends BaseModel {

    /**
     * user id
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    /**
     * goods id
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "goods_id",nullable = false)
    private Goods goods;

    @ManyToOne
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;
}
