package com.bbyuworld.gagyebbyu.domain.s3.dto.response;

public record S3ResponseDto(String path) {
	public static S3ResponseDto from(String fileName) {
		return new S3ResponseDto(fileName);
	}
}
