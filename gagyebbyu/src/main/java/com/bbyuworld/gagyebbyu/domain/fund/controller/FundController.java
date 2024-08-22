package com.bbyuworld.gagyebbyu.domain.fund.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.fund.dto.response.FundOverViewDto;
import com.bbyuworld.gagyebbyu.domain.fund.service.FundService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fund")
@RequiredArgsConstructor
public class FundController {

	private final FundService fundService;

	@GetMapping(path = "/{coupleId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FundOverViewDto> getFund(@PathVariable long coupleId) {
		return ResponseEntity.ok(fundService.getFund(coupleId));
	}

}
