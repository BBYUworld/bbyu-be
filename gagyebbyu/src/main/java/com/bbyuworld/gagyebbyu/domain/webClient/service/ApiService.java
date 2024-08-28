package com.bbyuworld.gagyebbyu.domain.webClient.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bbyuworld.gagyebbyu.domain.webClient.dto.ExpenseCategoryDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

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
}