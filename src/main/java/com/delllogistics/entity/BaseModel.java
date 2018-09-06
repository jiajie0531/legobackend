package com.delllogistics.entity;

import com.delllogistics.entity.user.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class BaseModel {

    @Id
    @GeneratedValue(generator  = "SnowflakeId")
    @GenericGenerator(name = "SnowflakeId",strategy = "com.delllogistics.sequence.SnowflakeId")
    @Column(name = "id",nullable = false,unique = true)
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Version
    @Column(name = "version_", nullable = false)
    private Integer version;

    public BaseModel() {
    }


    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            createTime = new Date();
        }
        if (updateTime == null) {
            updateTime = new Date();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Date getCreateTime(){
        return createTime;
    }



    /**
     * 创建用户
     */
    @JoinColumn(name = "create_user_id")
    @OneToOne(fetch=FetchType.LAZY)
    private User createUser;

    /**
     * 修改用户
     */
    @JoinColumn(name = "update_user_id")
    @OneToOne(fetch=FetchType.LAZY)
    private User updateUser;



    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }
}
