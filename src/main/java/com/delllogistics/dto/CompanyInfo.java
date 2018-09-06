package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Administrator on 2017-11-6.
 */
@Setter
@Getter
public class CompanyInfo {
    private Long id;
    @NotEmpty(message="名称不能为空")
    private String name;
    private String address;
    private String contactUser;
    private String contactPhone;
    private Long parentId;
}
