package com.bbyuworld.gagyebbyu.domain.user.service;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.user.dto.LoginResponseDto;
import com.bbyuworld.gagyebbyu.domain.user.dto.UserAccountRequestDto;
import com.bbyuworld.gagyebbyu.domain.user.dto.UserDto;
import com.bbyuworld.gagyebbyu.domain.user.dto.UserResponseDto;
import com.bbyuworld.gagyebbyu.domain.user.entity.Gender;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.AccountDto;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.BadRequestException;
import com.bbyuworld.gagyebbyu.global.error.type.UserNotFoundException;
import com.bbyuworld.gagyebbyu.global.jwt.JwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${ssafy.api_key}")
    private String apiKey;

    @Transactional
    public boolean regist(UserDto dto){
        User user = userRepository.findUserByEmail(dto.getEmail());
        if(user!=null)throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        String userKey = null;
        try {
            userKey = createUserKey(dto.getEmail());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user = User.fromDto(dto);
        user.setApiKey(userKey);
        userRepository.save(user);
        return true;
    }
    public LoginResponseDto login(UserDto dto){
        User user = userRepository.findUserByEmail(dto.getEmail());
        if(user == null)throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        if(user.isDeleted())throw new BadRequestException(ErrorCode.USER_DELETE_ERROR);
        if(user.getPassword().equals(dto.getPassword())){
            log.info("Login success");
            JwtToken token = jwtTokenProvider.createToken(user.getUserId());
            user.setAccessToken(token.getAccessToken());
            user.setRefreshToken(token.getRefreshToken());
            user.setLogin(true);
            userRepository.save(user);
            LoginResponseDto loginResponseDto = LoginResponseDto.builder().
                    token(token).
                    is_first_login(false).
                    userId(user.getUserId()).
                    build();
            if(user.getAge() == null){
                loginResponseDto.set_first_login(true);
            }
            return loginResponseDto;
        }
        return null;
    }

    public boolean logout(Long userId){
        User user = userRepository.findUserById(userId);
        if(user == null)throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        user.setLogin(false);
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId){
        User user = userRepository.findUserById(userId);
        if(user==null)throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        logout(userId);
        user.setDeleted(true);
        user.setAccessToken(null);
        user.setRefreshToken(null);
        userRepository.save(user);
        return true;
    }
    public boolean searchUser(String email){
        User user = userRepository.findUserByEmail(email);
        if(user == null)return true;
        return false;
    }

    public UserDto findUserByEmail(String email){
        User user = userRepository.findUserByEmail(email);
        return user.toDto();
    }

    @Transactional
    public boolean saveAdditionalInfo(Long userId, UserAccountRequestDto dto){
        User user = userRepository.findUserById(userId);
        if(dto.getAdditionalInfo().getGender().equals("male")){
            user.setGender(Gender.M);
        }
        else{
            user.setGender(Gender.F);
        }
        user.setAge(dto.getAdditionalInfo().getAge());
        Long monthlyIncome = dto.getAdditionalInfo().getSalary() / 12L;
        user.setMonthlyIncome(monthlyIncome);
        user.setMonthlyTargetAmount((long) dto.getAdditionalInfo().getDesiredSpending());
        user.setOccupation(dto.getAdditionalInfo().getOccupation());
        user.setRegion(dto.getAdditionalInfo().getRegion());

        for (AccountDto accountDto : dto.getSelectedAccounts()) {
            AssetAccount assetAccount = new AssetAccount();
            assetAccount.setUser(user);
            assetAccount.setBankName(accountDto.getBankName());
            assetAccount.setBankCode(accountDto.getBankCode());
            assetAccount.setAmount(accountDto.getAccountBalance());
            assetAccount.setAccountNumber(accountDto.getAccountNo());
            assetAccount.setAmount(accountDto.getAccountBalance());
            assetAccount.setAccountType(convertToAccountType(accountDto.getAccountTypeName()));
            assetAccount.setOneTimeTransferLimit(accountDto.getOneTimeTransferLimit());
            assetAccount.setDailyTransferLimit(accountDto.getDailyTransferLimit());
            assetAccount.setIsHidden(false);
            assetAccount.setCreatedAt(LocalDateTime.now());
            assetAccount.setUpdatedAt(LocalDateTime.now());

            user.getAssets().add(assetAccount);
        }
        userRepository.save(user);
        return true;
    }

    private AccountType convertToAccountType(String accountTypeName) {
        switch (accountTypeName) {
            case "수시입출금":
                return AccountType.FREE_SAVINGS;
            case "예금":
                return AccountType.DEPOSIT;
            case "적금":
                return AccountType.SAVINGS;
            default:
                throw new IllegalArgumentException("Unknown account type: " + accountTypeName);
        }
    }





    private String createUserKey(String userEmail) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String searchResult = searchUser(client, userEmail);
            if (searchResult != null) {
                return searchResult;
            }
            return createNewUser(client, userEmail);
        }
    }

    private String searchUser(CloseableHttpClient client, String userEmail) throws IOException {
        String url = "https://finopenapi.ssafy.io/ssafy/api/v1/member/search";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("apiKey", apiKey);
        objectNode.put("userId", userEmail);

        httpPost.setEntity(new StringEntity(objectNode.toString()));

        try (CloseableHttpResponse response = client.execute(httpPost)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            System.out.println("Search response = " + jsonResponse);

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            String responseCode = rootNode.path("responseCode").asText();

            if ("E4003".equals(responseCode)){
                return null;
            }
            else{
                return rootNode.path("userKey").asText();
            }
        }
    }

    private String createNewUser(CloseableHttpClient client, String userEmail) throws IOException {
        String url = "https://finopenapi.ssafy.io/ssafy/api/v1/member/";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("apiKey", apiKey);
        objectNode.put("userId", userEmail);

        httpPost.setEntity(new StringEntity(objectNode.toString()));

        try (CloseableHttpResponse response = client.execute(httpPost)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            System.out.println("Create user response = " + jsonResponse);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode < 200 || statusCode >= 300) {
                throw new IOException("Failed to create user. Status code: " + statusCode);
            }

            UserResponseDto responseDto = objectMapper.readValue(jsonResponse, UserResponseDto.class);
            System.out.println("response Dto = " + responseDto);
            return responseDto.getUserKey();
        }
    }

}
