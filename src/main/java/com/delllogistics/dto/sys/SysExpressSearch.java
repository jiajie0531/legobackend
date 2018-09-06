package com.delllogistics.dto.sys;

import com.delllogistics.dto.BaseSearchModel;
import lombok.Getter;
import lombok.Setter;

/**
 *  快递公司
 * Created by calvin  2018/1/17
 */
@Setter
@Getter
public class SysExpressSearch extends BaseSearchModel {
    /**
     * 快递公司名称
     */
    private String name;

    /**
     * 是否可用
     */
    private boolean isUsed;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }
}