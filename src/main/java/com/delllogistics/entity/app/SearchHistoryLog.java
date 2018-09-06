package com.delllogistics.entity.app;

import com.delllogistics.entity.BaseModel;
import com.delllogistics.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Search History Log.<br/>
 * User: jiajie<br/>
 * Date: 19/11/2017<br/>
 * Time: 8:28 PM<br/>
 */
@Entity
@Getter
@Setter
public class SearchHistoryLog extends BaseModel {

    /**
     * 查询内容
     */
    @NotNull(message = "查询内容不能为空")
    @Size(min = 1, max = 48, message = "查询内容长度必须在1和48之间")
    @Column(nullable = false)
    private String content;

    /**
     * user
     */
    @OneToOne
    @JoinColumn
    private User user;
}
