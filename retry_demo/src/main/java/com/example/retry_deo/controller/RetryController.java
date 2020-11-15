package com.example.retry_deo.controller;

import com.example.retry_deo.service.RetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class RetryController {

    @Resource
    private RetryService retryService;

    @PostMapping("/test")
    public String retryTest(@RequestParam int num) throws Exception {
        int remainingnum = retryService.getRemainingAmount(num);
        log.info("剩余数量===" + remainingnum);
        return "success";
    }
}
