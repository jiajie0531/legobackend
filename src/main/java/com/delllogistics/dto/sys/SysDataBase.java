package com.delllogistics.dto.sys;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *  数据库信息
 * Created by calvin  2018/1/17
 */
@Setter
@Getter
public class SysDataBase {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段名
     */
    private String columnName;
    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 注解
     */
    private String columnComment;
    /**
     * 约束
     */
    private String constraintName;

    /**
     * 关联表
     */
    private String referencedTableName;

    /**
     * 关联字段
     */
    private String referencedColumnName;

    /**
     * 数据库
     */
    private String tableSchema;
}