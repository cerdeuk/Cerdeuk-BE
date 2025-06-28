package org.sopt.certi_server.global.handler;

import java.io.IOException;

import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.dto.ErrorResponse;
import org.sopt.certi_server.global.error.exception.BusinessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;



import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
			.body(ErrorResponse.of(ex.getErrorCode()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return buildErrorResponse(ErrorCode.INVALID_FIELD_ERROR, ex.getBindingResult());
	}

	@ExceptionHandler(ConstraintDeclarationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolation ex) {
		return buildErrorResponse(ErrorCode.INVALID_FIELD_ERROR, ex.getMessage());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
		MissingServletRequestParameterException ex) {
		return buildErrorResponse(ErrorCode.MISSING_PARAMETER, ex.getParameterName());
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
		return buildErrorResponse(ErrorCode.MISSING_HEADER, ex.getHeaderName());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		String detail = ex.getRequiredType() != null
			? String.format("'%s'은(는) %s 타입이어야 합니다.", ex.getName(), ex.getRequiredType().getSimpleName())
			: "타입 변환 오류입니다.";
		return buildErrorResponse(ErrorCode.TYPE_MISMATCH, detail);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return buildErrorResponse(ErrorCode.DATA_INTEGRITY_VIOLATION, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		return buildErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());

	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(HandlerMethodValidationException ex) {
		return buildErrorResponse(ErrorCode.INVALID_FIELD_ERROR, ex.getMessage());
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode, Object detail) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode, detail));
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIoException(IOException ex) {
		return ResponseEntity.status(500).body("파일 처리 중 오류 발생: " + ex.getMessage());
	}
}
