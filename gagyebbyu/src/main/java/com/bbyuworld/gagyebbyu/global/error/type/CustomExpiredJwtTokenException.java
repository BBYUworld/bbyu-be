package com.bbyuworld.gagyebbyu.global.error.type;

import com.bbyuworld.gagyebbyu.global.error.ErrorCode;

public class CustomExpiredJwtTokenException extends BusinessException {
    public CustomExpiredJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CustomExpiredJwtTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public CustomExpiredJwtTokenException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
