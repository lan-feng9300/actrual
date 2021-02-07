package com.example.validation.exception;

import com.example.validation.entity.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@RestControllerAdvice(basePackages = "com.example.validation.controller")
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){

        logger.error("数据校验出问题"+e.getMessage()+e.getClass());
        Map<String, String> map = new HashMap<>();
        BindingResult result = e.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError err: errors) {
            String field = err.getField();
            String message = err.getDefaultMessage();
            map.put(field, message);
        }
        return R.error(400, "请求参数不合法, 请重新输入").put("data", map);
    }

    /**
     * 处理其他, 检验注解异常 如: @Size, @Max 等
     * 主要是将: @Size( message = "参数过长" ), 放入到返回值中
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R handleConstrainViolationException(ConstraintViolationException e){
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        if (!CollectionUtils.isEmpty(violations)) {
            StringBuffer msgBuffer = new StringBuffer();
            for (ConstraintViolation<?> c : violations) {
                msgBuffer.append(c.getMessage()).append(",");
            }
            String msg = msgBuffer.toString();
            if (msg.length() > 1) {
                msg.substring(0, msg.length()-1);
            }

            return R.error(400, msg);
        }

        return R.error(400, e.getMessage());

    }


    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        logger.error("全局异常:{}", throwable.getMessage());
        return R.error(500, "服务器内部异常");
    }

}
