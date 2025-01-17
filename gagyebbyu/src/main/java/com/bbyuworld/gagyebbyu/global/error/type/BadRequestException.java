package com.bbyuworld.gagyebbyu.global.error.type;

import com.bbyuworld.gagyebbyu.global.error.ErrorCode;

public class BadRequestException extends BusinessException {

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}

	public BadRequestException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	public BadRequestException(ErrorCode errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
}
