package com.bbyuworld.gagyebbyu.domain.s3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.s3.dto.request.S3RequestDto;
import com.bbyuworld.gagyebbyu.domain.s3.dto.response.S3ResponseDto;
import com.bbyuworld.gagyebbyu.domain.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/s3")
@Slf4j
@RequiredArgsConstructor
public class S3Controller {
	private final S3Service s3Service;

	@PostMapping("/upload/{fileName}")
	@ResponseStatus(HttpStatus.OK)
	S3ResponseDto createPresignedUrl(@RequestBody S3RequestDto s3RequestDto, @PathVariable String fileName) {
		return s3Service.createPresignedUrl(s3RequestDto, fileName);
	}
}