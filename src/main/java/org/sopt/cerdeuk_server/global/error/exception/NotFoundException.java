package org.sopt.cerdeuk_server.global.error.exception;

import org.sopt.cerdeuk_server.global.error.code.ErrorCode;

public class NotFoundException extends BusinessException {
	public NotFoundException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
