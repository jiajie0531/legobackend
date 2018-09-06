package com.delllogistics.exception;


/**
 * 所有业务异常的枚举
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午5:04:51
 */
public enum BizExceptionEnum implements ServiceExceptionEnum {

    /**
     * token异常
     */
    TOKEN_EXPIRED(700, "token过期"),
    TOKEN_AUTH_ERROR(701, "token验证失败"),
    TOKEN_ERROR(702, "token格式异常"),
    TOKEN_UNKNOWN_ERROR(703, "token验证未知异常"),


    //数据校验
    CLASS_FORMAT_ERROR(7001, "格式错误"),
    VERIFY_ERROR(7002, "验证错误"),
    VERIFY_ERROR_COMPANY(7007, "企业验证错误"),
    VERIFY_ERROR_SYSFILE(7008, "文件验证错误"),
    VERIFY_ERROR_ENTITY(7009, "参数验证错误"),
    VERIFY_ERROR_SYSAREA(7009, "地址验证错误"),
    VERIFY_ERROR_ACCOUNT(7010, "账户验证错误"),



    SQLCONSTRAINTVIOLATION(20001, "数据进行了约束请检测是否重复"),


    HARGEAMOUNT_BIG(20001, "扣款金额大于押金金额!"),



    INSTANTIATION_ERROR(7003, "实例化错误"),
    ININITIALIZER_ERROR(7004, "初始化程序错误"),
    ILLEGAL_ACCESS_ERROR(7005, "违法访问错误"),
    SQLEXCEPTION_ERROR(7006, "数据操作异常"),


    /**
     * 签名异常
     */
    SIGN_ERROR(704, "签名验证失败"),
    JSON_ERROR(705, "签名JSON格式异常"),

    /**
     * 其他
     */
    NO_PERMITION(405, "权限异常"),
    NO_WECHAT_CONFIG(406,"系统微信配置异常"),
    NO_WECHAT_COMPANY(407,"系统微信配置无对应企业ID"),
    NO_User_COMPANY(408,"用户无对应企业ID"),
    AUTH_REQUEST_ERROR(400, "账号密码错误"),



    /*
    订单
     */
    ORDER_OUT_PAY_TIME(500, "订单支付超时"),
    NO_VALID_FINANCERECEIVABLE(501,"没有有效的收款单"),

    /*
    会员积分规则
     */
    POINT_ROLE_EXISTS(800,"积分规则已存在"),
    MEMBER_DIS_ERROR(801,"会员折扣错误"),
    MEMBER_DIS_AMOUNT_ERROR(802,"折扣金额错误"),
    PAY_AMOUNT_ERROR(803,"支付金额错误"),


    /*
    会员
     */
    USER_IS_NONE(1401, "会员不存在"),

    /*
       嗮图
     */
    EVALUATE_PICS_ERRO(1501, "嗮图错误"),

    /*
    库存
     */
    GOODS_STOCK_NOT_ENOUGH(850,"库存不足"),
    GOODS_STOCK_MUST_GREATER_ZERO(851,"库存不能小于0"),
    ADD_STOCK_MUST_GREATER_ZERO(852,"上架数量必须大于0"),
    ;



    BizExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
