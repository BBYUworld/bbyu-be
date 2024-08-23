package com.bbyuworld.gagyebbyu.domain.user.controller;

import com.bbyuworld.gagyebbyu.domain.user.dto.UserDto;
import com.bbyuworld.gagyebbyu.domain.user.service.UserService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     *
     * @param user
     * NOT NULL LIST
     * 이메일, 비밀번호, 이름, 주소, 핸드폰, 성별, 나이
     * @return
     */
    @PostMapping("/regist")
    public String regist(@RequestBody UserDto user) throws JsonProcessingException {
        System.out.println("User = "+user);
        userService.regist(user);
//        userService.test(user.getEmail());
        return "hello";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto user){
        boolean flag = userService.login(user);
        return "no";
    }

    @PostMapping("/logout")
    @RequireJwtToken
    public String logout(){
        Long userId = UserContext.getUserId();
        userService.logout(userId);
        System.out.println("userId = "+userId);
        return "logout";
    }

    @DeleteMapping("/delete")
    @RequireJwtToken
    public String deleteUser(){
        Long userId = UserContext.getUserId();
        userService.deleteUser(userId);
        return "delete";
    }
}
