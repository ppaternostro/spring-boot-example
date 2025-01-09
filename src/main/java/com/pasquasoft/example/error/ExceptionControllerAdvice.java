package com.pasquasoft.example.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.pasquasoft.example.exception.PatchConversionException;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExceptionControllerAdvice
{
  private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex)
  {
    return mapToApiErrorResponse(ex.getBindingResult());
  }

  @ExceptionHandler(value = BindException.class)
  public ResponseEntity<ApiErrorResponse> handleBindException(BindException ex)
  {
    return mapToApiErrorResponse(ex.getBindingResult());
  }

  @ExceptionHandler(value = EntityNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex)
  {
    LOG.info("Entity not found: " + ex.getMessage());
    List<ApiError> errors = Collections.singletonList(new ApiError("id", ex.getMessage()));
    return new ResponseEntity<ApiErrorResponse>(new ApiErrorResponse(errors), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex)
  {
    LOG.info("Path parameter invalid: " + ex.getMessage());
    List<ApiError> errors = Collections.singletonList(new ApiError("id", ex.getMessage()));
    return new ResponseEntity<ApiErrorResponse>(new ApiErrorResponse(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = PatchConversionException.class)
  public ResponseEntity<ApiErrorResponse> handlePatchConversionException(PatchConversionException ex)
  {
    LOG.info("Patch JSON invalid: " + ex.getMessage());
    List<ApiError> errors = Collections.singletonList(new ApiError("json-patch", ex.getMessage()));
    return new ResponseEntity<ApiErrorResponse>(new ApiErrorResponse(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
  {
    LOG.info("Request body invalid: " + ex.getMessage());
    List<ApiError> errors = Collections.singletonList(new ApiError("employee", ex.getMessage()));
    return new ResponseEntity<ApiErrorResponse>(new ApiErrorResponse(errors), HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ApiErrorResponse> mapToApiErrorResponse(BindingResult bindingResult)
  {
    List<ApiError> errors = new ArrayList<>();

    bindingResult.getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorCode = error.getCode();
      errors.add(new ApiError(fieldName, errorCode));
    });

    return ResponseEntity.badRequest().body(new ApiErrorResponse(errors));
  }

}
