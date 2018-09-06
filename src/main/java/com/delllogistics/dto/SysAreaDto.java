package com.delllogistics.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author JiaJie
 * @create 2017-12-09 1:40 PM
 **/
@Getter
@Setter
public class SysAreaDto {
    private Long id;
    private String name;
    private int sort;
    private Long parentId;

    public SysAreaDto() {
    }

    public SysAreaDto(Long id, String name, int sort, Long parentId) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.parentId = parentId;
    }
}
