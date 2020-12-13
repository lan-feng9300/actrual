package com.example.validation.entity;


import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    public R (){
        put("code",200);
        put("message","successful");
    }

    public static R error(int code, String msg){
        R r = new R();
        r.put("code", code);
        r.put("message",msg);
        return r;
    }

    public static R error(){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "服务器内部异常, 请联系管理员");
    }

    public static R error(String message){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, message);
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
