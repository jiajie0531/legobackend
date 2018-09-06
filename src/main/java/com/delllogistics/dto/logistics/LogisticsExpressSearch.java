package com.delllogistics.dto.logistics;

import com.delllogistics.dto.BaseSearchModel;
import lombok.Getter;
import lombok.Setter;

/**
 *  快递管理
 * Created by calvin  2018/1/17
 */
@Setter
@Getter
public class LogisticsExpressSearch extends BaseSearchModel {
    /**
     * 快递名称
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