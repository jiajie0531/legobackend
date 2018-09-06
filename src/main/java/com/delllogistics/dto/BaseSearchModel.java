package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class BaseSearchModel {
    /**
     * 页数
     */
    private int page;
    /**
     * 大小
     */
    private int size;

    /**
     * 企业id
     */
    private Long companyId;
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

}
