package com.bbyuworld.gagyebbyu.domain.user.service;

import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetDeposit;
import com.bbyuworld.gagyebbyu.domain.asset.entity.Type;
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
import com.fasterxml.jackson.core.JsonProcessingException;
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
            LoginResponseDto loginResponseDto = LoginResponseDto.builder().token(token).is_first_login(false).build();
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

    @Transactional
    public boolean saveAdditionalInfo(Long userId, UserAccountRequestDto dto){
        User user = userRepository.findUserById(userId);
        if(dto.getAdditionalInfo().getGender().equals("M")){
            user.setGender(Gender.M);
        }
        else{
            user.setGender(Gender.F);
        }
        user.setAge(dto.getAdditionalInfo().getAge());
        Long monthlyIncome = dto.getAdditionalInfo().getSalary() / 12L;
        user.setMonthlyIncome(monthlyIncome);
        user.setMonthlyTargetAmount((long) dto.getAdditionalInfo().getDesiredSpending());

        for(AccountDto accountDto : dto.getSelectedAccounts()){
            Asset asset = new Asset();
            if(accountDto.getAccountTypeName().equals("수시입출금"))
                asset.setType(Type.DEPOSIT);
            asset.setBankName(accountDto.getBankName());
            asset.setAmount(accountDto.getAccountBalance());
            asset.setCreatedAt(LocalDateTime.now());
            asset.setUpdatedAt(LocalDateTime.now());
            asset.setUser(user);
            AssetDeposit assetDeposit = new AssetDeposit();
            assetDeposit.setBank(accountDto.getBankName());
            assetDeposit.setBankCode(accountDto.getBankCode());
            assetDeposit.setNumber(accountDto.getAccountNo());
            assetDeposit.setType(accountDto.getAccountTypeName());
            assetDeposit.setAmount(accountDto.getAccountBalance());
            assetDeposit.setHidden(false);
            asset.setAssetDeposit(assetDeposit);
            assetDeposit.setAsset(asset);
            assetDeposit.setOneTimeTransferLimit(accountDto.getOneTimeTransferLimit());
            assetDeposit.setDailyTransferLimit(accountDto.getDailyTransferLimit());
            user.getAssets().add(asset);
        }
        userRepository.save(user);
        return true;
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
