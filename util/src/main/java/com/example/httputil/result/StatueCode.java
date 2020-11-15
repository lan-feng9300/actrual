package com.example.httputil.result;

public enum StatueCode {

    SUCCESS(200,"成功"),
    PARAM_ERROR(400,"参数错误"),
    AUTH_ERROR(401,"认证失败"),
    UNKNOWN_ERROR(499,"未知错误"),
    SYS_ERROR(500,"系统错误"),
    FAILED(601,"请求失败");

    private int code;
    private String message;

    private StatueCode(int code, String message) {
        this.code=code;
        this.message=message;
    }

    public String getMessage() {
        return this.message;
    }
    public int getCode() {
        return this.code;
    }

}
