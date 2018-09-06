package com.delllogistics.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditsOut {
    private Long id;
    private Long score;
    private String status;
    private String type;
    private Long createTime;
    private Long updateTime;
    private String username;
}
