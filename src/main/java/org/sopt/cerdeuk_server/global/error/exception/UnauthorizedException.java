package org.sopt.cerdeuk_server.global.error.exception;

import org.sopt.cerdeuk_server.global.error.code.ErrorCode;

public class UnauthorizedException extends BusinessException {
	public UnauthorizedException() {
		super(ErrorCode.UNAUTHORIZED);
	}

	public UnauthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}

}
