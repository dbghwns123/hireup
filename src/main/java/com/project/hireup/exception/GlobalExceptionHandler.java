package com.project.hireup.exception;

import static com.project.hireup.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.project.hireup.type.ErrorCode.INVALID_REQUEST;

import com.project.hireup.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HireUpException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleHireUpException(HireUpException e) {
    log.error("{} is occurred.", e.getErrorCode());

    return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("Validation failed: {}", e.getMessage());

    // 첫 번째 유효성 검증 에러 메시지를 가져옴 (모든 에러 메시지로 반환할지 결정도 가능)
    String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();

    return new ErrorResponse(INVALID_REQUEST, errorMessage);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleException(Exception e) {
    log.error("Exception is occurred.", e);

    return new ErrorResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getDescription());
  }

}
