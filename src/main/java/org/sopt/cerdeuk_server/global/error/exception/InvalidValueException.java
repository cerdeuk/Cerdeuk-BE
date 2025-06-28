package org.sopt.cerdeuk_server.global.error.exception;

import org.sopt.cerdeuk_server.global.error.code.ErrorCode;

public class InvalidValueException extends BusinessException {
	public InvalidValueException(ErrorCode errorCode) {
		super(errorCode);
	}
}
