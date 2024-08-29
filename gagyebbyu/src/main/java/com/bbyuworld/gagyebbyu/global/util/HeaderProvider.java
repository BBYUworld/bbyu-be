package com.bbyuworld.gagyebbyu.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class HeaderProvider {
    public static ObjectNode createHeaderNode(String apiName, ObjectMapper objectMapper, String apiKey) {
        LocalDateTime now = LocalDateTime.now();
        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        System.out.println("transmissionDate: " + transmissionDate);
        System.out.println("transmissionTime: " + transmissionTime);
        ObjectNode headerNode = objectMapper.createObjectNode();
        headerNode.put("apiName", apiName);
        headerNode.put("transmissionDate", transmissionDate);
        headerNode.put("transmissionTime", transmissionTime);
        headerNode.put("institutionCode", "00100");
        headerNode.put("apiServiceCode", apiName);
        headerNode.put("fintechAppNo", "001");
        headerNode.put("institutionTransactionUniqueNo", generateUniqueNo());
        headerNode.put("apiKey", apiKey);

        return headerNode;
    }

    public static String generateUniqueNo() {
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = String.format("%06d", new Random().nextInt(1000000));
        return dateTime + randomPart;
    }
}
