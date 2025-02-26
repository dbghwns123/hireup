package com.project.hireup.exception;

import static com.project.hireup.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.project.hireup.type.ErrorCode.INVALID_REQUEST;

import com.project.hireup.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HireUpException.class)
  public ErrorResponse handleHireUpException(HireUpException e) {
    log.error("{} is occurred.", e.getErrorCode());

    return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException is occurred.", e);

    return new ErrorResponse(INVALID_REQUEST, INVALID_REQUEST.getDescription());
  }

  @ExceptionHandler(Exception.class)
  public ErrorResponse handleException(Exception e) {
    log.error("Exception is occurred.", e);

    return new ErrorResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getDescription());
  }

}
