package com.example.validation.controller;

import com.example.validation.entity.User;
import com.example.validation.validate.AddGroup;
import com.example.validation.validate.UpdateGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValiteControllerTest {

    @PostMapping("/addTest")
    public void addTest(@Validated(AddGroup.class) @RequestBody User user){

    }

    @PostMapping("/updateTest")
    public void updateTest(@Validated(UpdateGroup.class) @RequestBody User user){

    }
}
