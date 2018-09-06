package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResizeImg {

    /**
     * 宽度
     */
    private int width;

    /**
     * 高度
     */
    private int hight;

    /**
     * 最大高度
     */
    private int maxHight;

    /**
     * 最大宽度
     */
    private int maxWidth;

}
