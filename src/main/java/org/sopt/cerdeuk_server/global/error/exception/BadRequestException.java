package org.sopt.cerdeuk_server.global.error.exception;

import org.sopt.cerdeuk_server.global.error.code.ErrorCode;

public class BadRequestException extends BusinessException {
	public BadRequestException() {
		super(ErrorCode.BAD_REQUEST_DATA);
	}

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}

