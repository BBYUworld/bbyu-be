package com.bbyuworld.gagyebbyu.domain.user.controller;

import com.bbyuworld.gagyebbyu.domain.user.service.AccountService;
import com.bbyuworld.gagyebbyu.domain.user.service.UserService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/account")
@Slf4j
public class UserLedgerController {
    private final UserService userService;
    private final AccountService accountService;

    @GetMapping("/ledger")
    @RequireJwtToken
    public ResponseEntity<String> findAllUserAccount() {
        Long userId = UserContext.getUserId();
        return null;
    }
}
