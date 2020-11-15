package com.example.exception.test.controller;

import com.example.test.domain.ErrorResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeoutController {

    @GetMapping("/geterror")
    public ErrorResp getError(){


    }
}
