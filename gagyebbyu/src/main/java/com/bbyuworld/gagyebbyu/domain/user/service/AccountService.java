package com.bbyuworld.gagyebbyu.domain.user.service;

import com.bbyuworld.gagyebbyu.domain.user.dto.AccountDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Value("${ssafy.api_key}")
    private String apiKey;

    public List<AccountDto> findAllUserAccount(String email){

        return null;
    }

    private List<AccountDto> sendPost(){
        String url = "https://finopenapi.ssafy.io/ssafy/api/v1/member/search";
        return null;
    }
}
