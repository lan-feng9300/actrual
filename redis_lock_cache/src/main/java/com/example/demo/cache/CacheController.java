package com.example.demo.cache;

import com.example.demo.Service.CacheService;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CacheController {

    @Autowired
    CacheService cacheService;

    @GetMapping("/queryList")
    public List<User> queryList(){
        List<User> users = cacheService.selectUserInfo();
        return users;
    }

    @GetMapping("/editInfo")
    public boolean editInfo(@RequestParam String userName){
        return cacheService.updateUserInfo(userName);
    }
}
