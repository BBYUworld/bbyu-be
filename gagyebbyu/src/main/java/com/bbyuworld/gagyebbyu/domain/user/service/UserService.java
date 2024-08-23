package com.bbyuworld.gagyebbyu.domain.user.service;

import com.bbyuworld.gagyebbyu.domain.user.dto.UserDto;
import com.bbyuworld.gagyebbyu.domain.user.dto.UserResponseDto;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
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
    public boolean login(UserDto dto){
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
            return true;
        }
        return false;
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

    public void test(String userEmail) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("apiKey", apiKey);
        objectNode.put("userId", userEmail);
        String json = objectMapper.writeValueAsString(objectNode);
        System.out.println("결과 = "+json);
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