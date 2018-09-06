package com.delllogistics.entity.app;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.entity.user.User;
import com.delllogistics.entity.goods.Goods;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 足迹.<br/>
 * User: jiajie<br/>
 * Date: 09/12/2017<br/>
 * Time: 10:30 PM<br/>
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update browsing_history set is_deleted=1,update_time=now() where id=? and version_=?")
public class BrowsingHistory extends BaseModel {

    /**
     * user id
     */
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    /**
     * goods id
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    private Goods goods;

    @ManyToOne
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;

    private int count;
}
