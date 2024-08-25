package com.bbyuworld.gagyebbyu.domain.user.service;

import com.bbyuworld.gagyebbyu.domain.user.dto.UserResponseDto;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.AccountDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.DemandDepositDto;
import com.bbyuworld.gagyebbyu.global.util.ApiPost;
import com.bbyuworld.gagyebbyu.global.util.ApiResponse;
import com.bbyuworld.gagyebbyu.global.util.HeaderProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Value("${ssafy.api_key}")
    private String apiKey;
    private final ApiPost apiPost;
    private final UserRepository userRepository;

    public List<DemandDepositDto> findAllDemandDeposit(){
        return sendPostAboutFindAllProducts();
    }
    public List<AccountDto> findAllUserAccount(Long userId){
        User user = userRepository.findUserById(userId);
        String userKey = user.getApiKey();
        List<AccountDto> list = sendPostAboutUserAccount(userKey);
        return list;
    }
    private List<AccountDto> sendPostAboutUserAccount(String userKey){
        try(CloseableHttpClient client = HttpClients.createDefault()){
            String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/inquireDemandDepositAccountList";
            String apiName = "inquireDemandDepositAccountList";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, objectMapper, apiKey);
            headerNode.put("userKey", userKey);
            rootNode.set("Header", headerNode);
            String jsonBody = objectMapper.writeValueAsString(rootNode);
            System.out.println("Request Body: " + jsonBody);
            httpPost.setEntity(new StringEntity(jsonBody));

            try(CloseableHttpResponse response = client.execute(httpPost)){
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JsonNode responseRootNode = objectMapper.readTree(jsonResponse);
                JsonNode recNode = responseRootNode.get("REC");
                System.out.println("Response = "+jsonResponse);
                if (recNode.isArray()) {
                    return objectMapper.convertValue(recNode, new TypeReference<List<AccountDto>>() {});
                } else {
                    return new ArrayList<>();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private List<DemandDepositDto> sendPostAboutFindAllProducts(){
        System.out.println("Call Find All Products");
        try(CloseableHttpClient client = HttpClients.createDefault()){
            String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/inquireDemandDepositList";
            String apiName = "inquireDemandDepositList";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, objectMapper, apiKey);
            rootNode.set("Header", headerNode);
            String jsonBody = objectMapper.writeValueAsString(rootNode);
            System.out.println("Request Body: " + jsonBody);
            httpPost.setEntity(new StringEntity(jsonBody));

            try(CloseableHttpResponse response = client.execute(httpPost)){
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JsonNode responseRootNode = objectMapper.readTree(jsonResponse);
                JsonNode recNode = responseRootNode.get("REC");
                System.out.println("recNode = "+recNode);
                if (recNode.isArray()) {
                    return objectMapper.convertValue(recNode, new TypeReference<List<DemandDepositDto>>() {});
                } else {
                    return new ArrayList<>();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
