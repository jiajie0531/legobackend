package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/** 上级机构授权下级机构商品
 * Created by xzm on 2017-11-17.
 */
@Setter
@Getter
public class TransferInfo {
    private Set<BaseTransfer> mockData;
    private Set<String> targetKeys;
}
