package com.example.retry_deo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;


@Service
public class RetryService {

    private static Logger log = LoggerFactory.getLogger(RetryService.class);
    private final static int TOTAL_NUM = 100000;

    /**
     *
     *    @Retryable：标记当前方法使用重试机制
     * 　　value：触发重试机制的条件，当遇到Exception时，会重试
     * 　　maxAttempts ：设置最大重试次数，默认为3次
     * 　　delay：重试延迟时间，单位毫秒，即距离上一次重试方法的间隔
     * 　　multiplier：delay重试延迟时间的间隔倍数，即第一次为5秒，第二次为5乘以2为10秒，依此类推
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 2))
    public int getRemainingAmount(int num) throws Exception {
        log.info("getRemainingAmount======" + LocalTime.now());
        if (num <= 0) {
            throw new Exception("数量不对");
        }
        log.info("getRemainingAmount======执行结束");
        return TOTAL_NUM - num;
    }

    @Recover
    public int recover(Exception e){
        // 重试失败, 回补办法
        return -1; 
    }
}
