package com.delllogistics.exception;

/**异常错误码和消息定义
 * Created by xzm on 2018-1-18.
 */
public enum GeneralExceptionEnum implements ServiceExceptionEnum{

    /**
     * 其他
     */
    WRITE_ERROR(500,"渲染界面错误"),

    ;

    GeneralExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
