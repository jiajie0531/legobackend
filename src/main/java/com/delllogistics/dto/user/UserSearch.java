package com.delllogistics.dto.user;

import com.delllogistics.entity.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserSearch {

    private int page;

    private int size;
    /**
     * 用户名称
     */
    private String username;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 用户类型
     */
    private UserType userType;
}
