package com.bbyuworld.gagyebbyu.domain.user.controller;

import com.bbyuworld.gagyebbyu.domain.user.dto.UserDto;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    @GetMapping("/regist")
    public String regist(){
        System.out.println("Come to regist");
        return "hello";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto user){
        System.out.println("user = "+user);
        return "no";
    }
}
