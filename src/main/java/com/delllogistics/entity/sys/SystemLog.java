package com.delllogistics.entity.sys;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**系统日志
 * Created by xzm on 2018-3-13.
 */
@Entity
@Getter
@Setter
public class SystemLog {
    @Id
    @GeneratedValue(generator  = "SnowflakeId")
    @GenericGenerator(name = "SnowflakeId",strategy = "com.delllogistics.sequence.SnowflakeId")
    @Column(name = "id")
    private Long id;
    /**
     * 创建时间
     */

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    /**
     * 用户名
     */

    private String username;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 操作内容
     */
    private String operation;
    /**
     * 请求类型
     */
    private String requestMethod;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 用户浏览器
     */
    private String userAgent;
    /**
     * 返回结果
     */
    private String resultMsg;
    /**
     * 响应时间
     */
    private Long spendTime;


    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            createTime = new Date();
        }
    }
}

