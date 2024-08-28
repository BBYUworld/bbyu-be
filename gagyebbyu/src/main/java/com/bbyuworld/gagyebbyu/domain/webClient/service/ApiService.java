package com.bbyuworld.gagyebbyu.domain.webClient.service;

import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendDepositRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendLoanRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendSavingsRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bbyuworld.gagyebbyu.domain.webClient.dto.ExpenseCategoryDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
@Service
public class ApiService {

	private final WebClient webClient;

	public ApiService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
	}

	public String sendPostRequest(String url, ExpenseCategoryDto requestBody) {
		try {
			String response = this.webClient.post()
				.uri(url)
				.body(Mono.just(requestBody), ExpenseCategoryDto.class)
				.retrieve()
				.bodyToMono(String.class)
				.block();

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(response);
			return jsonNode.get("category").asText();

		} catch (Exception e) {
			throw new RuntimeException("Error during API call", e);
		}
	}



	public List<Map.Entry<Integer, Double>> sendLoanPostRequest(String url, RecommendLoanRequestDto requestBody) {
		try {
			String response = this.webClient.post()
					.uri(url)
					.body(Mono.just(requestBody), RecommendLoanRequestDto.class)
					.retrieve()
					.bodyToMono(String.class)
					.block();

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(response);

			// top_5_loan_products의 JSON 배열을 파싱
			List<Map.Entry<Integer, Double>> loanProducts = new ArrayList<>();
			JsonNode loanProductsNode = jsonNode.get("top_5_loan_products");

			// 각 요소를 리스트로 변환하여 저장
			for (JsonNode productNode : loanProductsNode) {
				Integer id = productNode.get(0).asInt();
				Double pred = productNode.get(1).asDouble();
				loanProducts.add(new AbstractMap.SimpleEntry<>(id, pred));
			}

			return loanProducts;

		} catch (Exception e) {
			throw new RuntimeException("Error during API call", e);
		}
	}

	public List<Map.Entry<Integer, Double>> sendDepositPostRequest(String url, RecommendDepositRequestDto requestBody) {
		try {
			String response = this.webClient.post()
					.uri(url)
					.body(Mono.just(requestBody), RecommendDepositRequestDto.class)
					.retrieve()
					.bodyToMono(String.class)
					.block();

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(response);

			// top_5_loan_products의 JSON 배열을 파싱
			List<Map.Entry<Integer, Double>> depositProducts = new ArrayList<>();
			JsonNode depositProductsNode = jsonNode.get("top_5_deposit_products");

			// 각 요소를 리스트로 변환하여 저장
			for (JsonNode productNode : depositProductsNode) {
				Integer id = productNode.get(0).asInt();
				Double pred = productNode.get(1).asDouble();
				depositProducts.add(new AbstractMap.SimpleEntry<>(id, pred));
			}

			return depositProducts;

		} catch (Exception e) {
			throw new RuntimeException("Error during API call", e);
		}
	}
	public List<Map.Entry<Integer, Double>> sendSavingsPostRequest(String url, RecommendSavingsRequestDto requestBody) {
		try {
			String response = this.webClient.post()
					.uri(url)
					.body(Mono.just(requestBody), RecommendSavingsRequestDto.class)
					.retrieve()
					.bodyToMono(String.class)
					.block();

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(response);

			// top_5_loan_products의 JSON 배열을 파싱
			List<Map.Entry<Integer, Double>> savingsProducts = new ArrayList<>();
			JsonNode savingsProductsNode = jsonNode.get("top_5_savings_products");

			// 각 요소를 리스트로 변환하여 저장
			for (JsonNode productNode : savingsProductsNode) {
				Integer id = productNode.get(0).asInt();
				Double pred = productNode.get(1).asDouble();
				savingsProducts.add(new AbstractMap.SimpleEntry<>(id, pred));
			}

			return savingsProducts;

		} catch (Exception e) {
			throw new RuntimeException("Error during API call", e);
		}
	}


}