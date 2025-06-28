package org.sopt.cerdeuk_server.global.error.exception;

import org.sopt.cerdeuk_server.global.error.code.ErrorCode;

public class ForbiddenException extends BusinessException {
	public ForbiddenException(ErrorCode errorCode) {
		super(errorCode);
	}
}

