package com.example.exception.test.exception;

public class TimeoutException extends RuntimeException{

    private static final long serialVersionUID = 4564124491192825748L;

    private int code;

    public TimeoutException() {
        super();
    }

    public TimeoutException(int code, String message) {
        super(message);
        this.setCode(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
