package com.delllogistics.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 个人积分来源.<br/>
 * User: jiajie<br/>
 * Date: 27/04/2018<br/>
 * Time: 7:47 AM<br/>
 */
@ToString
@Getter
@Setter
public class MembershipPointAggregate {
    private Long userId;
    private String membershipPointType;
    private String rule;
    private BigDecimal points;
}
