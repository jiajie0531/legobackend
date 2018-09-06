package com.delllogistics.controller.sys;

import com.delllogistics.dto.Result;
import com.delllogistics.service.sys.SysDataBaseService;
import com.delllogistics.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysDataBase")
public class SysDataBaseController {


    private final SysDataBaseService sysDataBaseService;


    @Autowired
    public SysDataBaseController(SysDataBaseService sysDataBaseService) {
        this.sysDataBaseService = sysDataBaseService;
    }


    /**
     * 获取数据库表信息
     * @param tableSchema 数据库
     * @param tableName  表名
     * @return 数据库表信息
     */
    @GetMapping("/findList")
    public Page findList(String tableSchema, String tableName, boolean isNullCheck, String columnName, int page, int size) {
        return sysDataBaseService.findDataBase(tableSchema, tableName,isNullCheck, columnName,page,size);
    }
    /**
     * 获取数据库下的表
     *
     * @param tableSchema 数据库
     * @return 数据库表信息
     */
    @GetMapping("/findTable")
    public  List  findTable(String tableSchema, String tableName) {
        return sysDataBaseService.findTable(tableSchema,tableName);
    }



    /**
     * 修改数据库表字段注解
     *
     * @param columnName 字段名
     * @param tableName 表名
     * @param columnDefault 默认值
     * @param columnType 字段类型
     * @param isNullable 是否为空
     * @param extra 自增
     * @param columnComment 注释
     * @return 数据库表信息
     */
    @GetMapping("/saveComment")
    public Result saveComment(String columnName, String tableName, String columnDefault, String columnType,String isNullable, String extra, String columnComment,boolean tableCommentChecked,String tableComment) {

        int rst = sysDataBaseService.saveComment( columnName,  tableName,  columnDefault,  columnType,isNullable,  extra,  columnComment,tableCommentChecked,tableComment);
        if(rst<1){
            return ResultUtil.error(1,"修改错误");
        }
        return ResultUtil.success();
    }

}
