package com.example.exception.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  异常信息返回类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResp {

    private int code;
    private String message;
}