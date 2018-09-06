package com.delllogistics.dto.app;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCount {

    private int countWaitToPay;
    private int countPaid;
    private int countWaitToDelivery;
    private int countDelivery;
}
