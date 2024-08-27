package com.bbyuworld.gagyebbyu.domain.user.controller;

import com.bbyuworld.gagyebbyu.domain.user.dto.LoginResponseDto;
import com.bbyuworld.gagyebbyu.domain.user.dto.UserAccountRequestDto;
import com.bbyuworld.gagyebbyu.domain.user.dto.UserDto;
import com.bbyuworld.gagyebbyu.domain.user.service.AccountService;
import com.bbyuworld.gagyebbyu.domain.user.service.UserService;
import com.bbyuworld.gagyebbyu.global.api.asset.CreateDemandDepositAccountDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.AccountDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.DemandDepositDto;
import com.bbyuworld.gagyebbyu.global.jwt.JwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
//    private final MailSender mailSender;

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

    @GetMapping("/account")
    @RequireJwtToken
    public ResponseEntity<List<AccountDto>> findAllUserAccount(){
        Long userId = UserContext.getUserId();
        List<AccountDto> list = accountService.findAllUserAccount(userId);
        System.out.println("List = "+list);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/account")
    @RequireJwtToken
    public ResponseEntity<AccountDto> createUserAccount(@RequestBody Map<String, String>map){
        Long userId = UserContext.getUserId();
        String uniqueNo = map.get("accountTypeUniqueNo");
        String bankName = map.get("bankName");
        Long dailyTransferLimit = Long.parseLong(map.get("dailyTransferLimit"));
        Long oneTimeTransferLimit = Long.valueOf(map.get("oneTimeTransferLimit"));
        System.out.println("bankName = "+bankName);
        System.out.println("uniqueNo = "+uniqueNo);
        System.out.println("dailyTransferLimit" +dailyTransferLimit);
        System.out.println("oneTimeTransferLimit" +oneTimeTransferLimit);
        AccountDto dto = accountService.createUserAccount(userId, uniqueNo, bankName, dailyTransferLimit, oneTimeTransferLimit);
        System.out.println("dto = "+dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/product")
    @RequireJwtToken
    public ResponseEntity<List<DemandDepositDto>> findAllProducts(){
        List<DemandDepositDto> list = accountService.findAllDemandDeposit();
        System.out.println("return list = "+list);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/adittional/info")
    @RequireJwtToken
    public ResponseEntity<String> saveAdditionalInfo(@RequestBody UserAccountRequestDto requestDto){
        Long userId = UserContext.getUserId();
        userService.saveAdditionalInfo(userId, requestDto);
        return ResponseEntity.ok("good");
    }

    @GetMapping("/find")
    @RequireJwtToken
    public ResponseEntity<UserDto> findUserByEmail(@RequestParam("email")String email){
        UserDto user = userService.findUserByEmail(email);
        return ResponseEntity.ok(user);
    }

//    @PostMapping("/email")
//    public ResponseEntity<String> emailVerification(@RequestBody UserDto dto){
//        System.out.println("email = "+dto.getEmail());
//        String authNumber =mailSender.joinEmail(dto.getEmail());
//        return ResponseEntity.ok(authNumber);
//    }
}
