package com.delllogistics.entity.sys;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by jiajie on 10/06/2017.
 */
@Getter
@Setter
@Entity
@Table(name = "phone_validate_code")
public class PhoneValidateCode extends BaseModel {
    private String phone;
    private int code;
    private boolean used;
}

