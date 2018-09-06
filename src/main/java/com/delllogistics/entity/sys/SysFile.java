
/**
 * Created by guangxingju on 2017/10/27.
 */
package com.delllogistics.entity.sys;

import com.alibaba.fastjson.annotation.JSONField;
import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SQLDelete(sql = "update sys_file set is_deleted=1,update_time=now() where id=? and version_=?")
public class SysFile extends BaseModel {
    /**
     * 上传文件后名称
     */
    private String uploadName;
    /**
     * 上传文件前名称
     */
    private String originalName;
    /**
     * 上传文件路径+文件
     */
    @Column(length = 1000)
    private String url;
    /**
     * 上传文件来源
     */
    private String source;
    /**
     * 上传文件路径
     */
    @Column(length = 1000,nullable = false)
    private String path;
    /**
     * 上传文件大小
     */
    private long size;
    /**
     * 上传文件类型
     */
    @Column(length = 100)
    private String type;

    /**
     * antd展示
     */
    private String uid;
    /**
     * 用户绑定
     */
    @JSONField(serialize = false)
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;


}

