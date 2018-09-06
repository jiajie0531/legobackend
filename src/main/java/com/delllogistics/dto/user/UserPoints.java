package com.delllogistics.dto.user;

import com.delllogistics.entity.enums.MembershipPointType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserPoints {

    private MembershipPointType membershipPointType;

    private BigDecimal points;

    public UserPoints() {};
    public UserPoints(MembershipPointType membershipPointType, BigDecimal points) {
        this.membershipPointType = membershipPointType;
        this.points = points;
    }
}
