package com.bbyuworld.gagyebbyu.global.util;

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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ApiPost {
    @Value("${ssafy.api_key}")
    private String apiKey;
    private final ObjectMapper objectMapper;
    public <T> T sendPostRequest(String url, String apiName,String userKey,T requestBody, Class<T> responseType) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");

            ObjectNode rootNode = objectMapper.createObjectNode();
            ObjectNode headerNode = createHeaderNode(apiName, userKey);
            rootNode.set("HEADER", headerNode);

            if (requestBody != null) {
                ObjectNode bodyNode = objectMapper.valueToTree(requestBody);
                rootNode.setAll(bodyNode);
            }

            String jsonBody = objectMapper.writeValueAsString(rootNode);
            httpPost.setEntity(new StringEntity(jsonBody));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                return objectMapper.readValue(jsonResponse, responseType);
            }
        }
    }

    private ObjectNode createHeaderNode(String apiName, String userKey) {
        LocalDateTime now = LocalDateTime.now();
        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));

        ObjectNode headerNode = objectMapper.createObjectNode();
        headerNode.put("apiName", apiName);
        headerNode.put("transmissionDate", transmissionDate);
        headerNode.put("transmissionTime", transmissionTime);
        headerNode.put("institutionCode", "001");
        headerNode.put("apiServiceCode", apiName);
        headerNode.put("fintechAppNo", "001");
        headerNode.put("institutionTransactionUniqueNo", generateUniqueNo());
        headerNode.put("apiKey", apiKey);
        headerNode.put("userKey", userKey);

        return headerNode;
    }

    private String generateUniqueNo() {
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = String.format("%06d", new Random().nextInt(1000000));
        return dateTime + randomPart;
    }
}
