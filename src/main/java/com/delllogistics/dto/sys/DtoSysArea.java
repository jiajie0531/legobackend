package com.delllogistics.dto.sys;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
public class DtoSysArea {
    private BigInteger value;
    private int level;
    private String label;
    private BigInteger pid;

    private int  sort;
    private boolean  checked;
    private boolean  disable;
    private boolean  hide;
    private boolean  isLeaf;
    public void setIsLeaf(boolean isUsed) {
        this.isLeaf = isUsed;
    }
    public boolean getIsLeaf() {
        return isLeaf;
    }

    private   List<DtoSysArea> children;

}
