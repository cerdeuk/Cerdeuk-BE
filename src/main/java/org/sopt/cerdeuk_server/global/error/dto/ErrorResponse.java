package org.sopt.cerdeuk_server.global.error.dto;

import java.util.List;
import java.util.Set;

import org.sopt.cerdeuk_server.global.error.code.ErrorCode;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.ConstraintViolation;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
	int status,
	String code,
	String message,
	List<ValidationError> errors
) {
	//에러 응답(유효성 검사 없음, ErrorCode 메세지만 표시)
	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.getHttpStatus().value(),
			errorCode.getCode(),
			errorCode.getMessage(),
			null
		);
	}

	//에러 응답(유효성 검사포함)
	public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
		return new ErrorResponse(
			errorCode.getHttpStatus().value(),
			errorCode.getCode(),
			errorCode.getMessage(),
			ValidationError.of(bindingResult)
		);
	}

	//에러 응답(ConstraintViolation 포함)
	public static ErrorResponse of(ErrorCode errorCode, Set<ConstraintViolation<?>> violations) {
		return new ErrorResponse(
			errorCode.getHttpStatus().value(),
			errorCode.getCode(),
			errorCode.getMessage(),
			ValidationError.of(violations)
		);
	}

	//에러 응답(추가 세부 정보 포함)
	public static ErrorResponse of(ErrorCode errorCode, Object detail) {
		return new ErrorResponse(
			errorCode.getHttpStatus().value(),
			errorCode.getCode(),
			errorCode.getMessage() + (detail != null ? ": " + detail : ""),
			null
		);
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record ValidationError(
		String path,
		String field,
		String message
	) {
		//ConstraintViolation -> ValidationError 변환
		public static List<ValidationError> of(Set<ConstraintViolation<?>> violations) {
			return violations == null ? List.of() : violations.stream()
				.map(violation -> new ValidationError(
					violation.getPropertyPath().toString(),
					null,
					violation.getMessage()
				))
				.toList();
		}

		//FieldError -> ValidationError 변환
		public static List<ValidationError> of(BindingResult bindingResult) {
			return bindingResult.getFieldErrors().stream()
				.map(error -> new ValidationError(
					null,
					error.getField(),
					error.getDefaultMessage()
				))
				.toList();
		}
	}

}
