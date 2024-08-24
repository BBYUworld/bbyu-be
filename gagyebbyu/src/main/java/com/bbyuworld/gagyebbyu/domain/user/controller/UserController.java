package com.bbyuworld.gagyebbyu.domain.user.controller;

import com.bbyuworld.gagyebbyu.domain.user.dto.LoginResponseDto;
import com.bbyuworld.gagyebbyu.domain.user.dto.UserDto;
import com.bbyuworld.gagyebbyu.domain.user.service.UserService;
import com.bbyuworld.gagyebbyu.global.jwt.JwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import com.bbyuworld.gagyebbyu.global.util.MailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final MailSender mailSender;

    /**
     *
     * @param user
     * NOT NULL LIST
     * 이메일, 비밀번호, 이름, 주소, 핸드폰, 성별, 나이
     * @return
     */
    @PostMapping("/regist")
    public ResponseEntity<String> regist(@RequestBody UserDto user) throws JsonProcessingException {
        System.out.println("User = "+user);
        userService.regist(user);
//        userService.test(user.getEmail());
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto user){
        LoginResponseDto loginResponseDto = userService.login(user);
        System.out.println("LoginResponseDto = "+loginResponseDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/logout")
    @RequireJwtToken
    public ResponseEntity<String> logout(){
        Long userId = UserContext.getUserId();
        userService.logout(userId);
        return ResponseEntity.ok("로그아웃 성공");
    }

    @DeleteMapping("/delete")
    @RequireJwtToken
    public ResponseEntity<String> deleteUser(){
        Long userId = UserContext.getUserId();
        userService.deleteUser(userId);
        return ResponseEntity.ok("계정 삭제 성공");
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchUser(@RequestParam("email") String email){
        boolean flag = userService.searchUser(email);
        if(flag){
            return ResponseEntity.ok("not Exist");
        }
        return ResponseEntity.ok("is Exist");
    }

    @PostMapping("/email")
    public ResponseEntity<String> emailVerification(@RequestBody UserDto dto){
        System.out.println("email = "+dto.getEmail());
        String authNumber =mailSender.joinEmail(dto.getEmail());
        return ResponseEntity.ok(authNumber);
    }
}
