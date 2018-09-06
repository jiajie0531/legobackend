package com.delllogistics.exception;

/**
 * Created by jiajie on 09/06/2017.
 */
public enum ExceptionCode {
    PARAM_TYPE_ERROR(1000),
    NOT_FOUND(1001),
    NO_PERMISSION(1002),
    SYSTEM(1003),
    SERVICE(1004),
    NO_HANDLER(1005),
    CONTENT_TYPE_ERROR(1006),
    CLASS_FULL(1007),
    DATE_INTERVAL_TOO_LONG(1008),
    NEED_LOGIN(1009),
    PHONE_VALIDATE_FAILED(1010),
    PHONE_EXISTED(1011),
    ORDER_CANCELLED(1012),
    NOT_PAID(1013),
    PAID_LESS_MONEY(1014),
    CLASS_BEGIN(1015),
    ACCOUNT_DISABLE(1016),
    GET_WECHATUSER_FAIL(1017),
    CONVERT_ENTITY_FAIL(1018),//反射转换entity异常
    USERNAME_EXISTS(1019),//用户名已存在
    PASSWORD_NULL(1020), //密码为空
    RESOURCE_NULL(1021), //权限为空
    TOKEN_INVALID(1022),//令牌失效
    GOODS_BRAND_NAME_NULL(2001), //密码为空
    COMPANYNAME_EXISTS(1023),//机构名称已存在
    GRANT_COMPANY_ID_SAME(1024),//分配机构与待分配机构不能相同
    GRANT_COMPANY_MUST_LESS(1025),//待分配机构必须为分配机构的下级
    ROLE_NAME_EXISTS(1026),//角色名已存在
    SHOPPINGCART_GOODS_NULL(1027),//加入购物车商品为空
    GOODS_DISABLE(1028),//无效商品


    INVALID_CANCEL_ORDER(1029),//无效的取消订单请求
    CANNOT_CANCEL_ORDER(1030),//订单已无法取消
    SYSAREA_ERROR(1031),//订单地址无效
    INVALID_ORDER(1030),//无效订单


    INVALID_PAY_ORDER(1032),//无效的支付订单请求
    CANNOT_PAY_ORDER(1033),//订单已无法支付


    INVALID_DELIVERY_ORDER(1034),//无效的确认收货请求
    CANNOT_DELIVERY_ORDER(1035),//订单已无法确认收货

    ORDER_SERVICE_EVALUATE_ERRO(1036),//物流服务评论已无法确认收货
    NOT_WECHAT_USER(1037), //非微信用户
    INVALID_WECHAT_PAY_REQUEST(1038),//无效的微信支付请求
    COMPANY_ID_INVALID(1040), //门店ID无效

    INVALID_ERRO(8000), //数据校验错误
    COMPLETED_ERRO(8001), //操作已完成不要重复提交
    NONEPIC_ERRO(8002), //请选择图片上传

    ;
    private int codeNumber;

    ExceptionCode(int codeNumber) {
        this.codeNumber = codeNumber;
    }

    public int getCodeNumber() {
        return codeNumber;
    }
}

