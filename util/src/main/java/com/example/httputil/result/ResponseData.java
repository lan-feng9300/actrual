package com.example.httputil.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  返回结果的封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> {

    private int status; // 状态码：200是正常调用 500是程序出错

    private String msg; // 返回消息：成功还是失败

    private T data; // 返回的对象。有时候我们要返回一些数据就放在这个里，如果不需要返回数据则是null

    /**
     * 对返回值的封装
     *
     * @param status
     * @param msg
     * @return
     */
    public static ResponseData result(int status, String msg, Object data) {
        return new ResponseData(status, msg, data);
    }
}
