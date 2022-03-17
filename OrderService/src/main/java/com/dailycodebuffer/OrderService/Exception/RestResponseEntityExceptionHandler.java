package com.dailycodebuffer.OrderService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dailycodebuffer.OrderService.external.response.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCException(CustomException exception) {
		return new ResponseEntity<>(new ErrorResponse().builder().errorMessage(exception.getMessage())
				.errorCode(exception.getErrorCode()).build(), HttpStatus.valueOf(exception.getStatus()));
	}
}
