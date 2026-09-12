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
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.pasquasoft.example.exception.PatchConversionException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

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
    LOG.warn("Entity not found: " + ex.getMessage());
    List<ApiError> errors = Collections.singletonList(new ApiError("id", ex.getMessage()));
    return new ResponseEntity<ApiErrorResponse>(new ApiErrorResponse(errors), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex)
  {
    String parameterName = ex.getName();
    LOG.warn("Invalid path parameter: {}={}", parameterName, ex.getValue());
    List<ApiError> errors = Collections.singletonList(new ApiError(parameterName, "Invalid"));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(errors));
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ApiErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex)
  {
    String contentType = ex.getContentType() != null ? ex.getContentType().toString() : "unknown";
    LOG.warn("Unsupported Content-Type: {}", contentType);
    List<ApiError> errors = Collections.singletonList(new ApiError("Content-Type", "Unsupported"));
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ApiErrorResponse(errors));
  }

  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ResponseEntity<ApiErrorResponse> handleMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex)
  {
    LOG.warn("Accept header not acceptable: {}", ex.getMessage());
    List<ApiError> errors = Collections.singletonList(new ApiError("Accept", "NotAcceptable"));
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ApiErrorResponse(errors));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiErrorResponse> handleMissingRequestParameter(MissingServletRequestParameterException ex)
  {
    String parameterName = ex.getParameterName();
    LOG.warn("Missing request parameter: {}", parameterName);
    List<ApiError> errors = Collections.singletonList(new ApiError(parameterName, "Required"));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(errors));
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<ApiErrorResponse> handleMissingRequestHeader(MissingRequestHeaderException ex)
  {
    String headerName = ex.getHeaderName();
    LOG.warn("Missing request header: {}", headerName);
    List<ApiError> errors = Collections.singletonList(new ApiError(headerName, "Required"));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(errors));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex)
  {
    LOG.warn("HTTP method not supported: {}", ex.getMethod());
    List<ApiError> errors = Collections.singletonList(new ApiError("method", "NotSupported"));
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ApiErrorResponse(errors));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request)
  {
    String path = request.getRequestURI();
    LOG.warn("Invalid path: {}", path);
    List<ApiError> errors = Collections.singletonList(new ApiError(path, "Invalid"));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(errors));
  }

  @ExceptionHandler(value = PatchConversionException.class)
  public ResponseEntity<ApiErrorResponse> handlePatchConversionException(PatchConversionException ex)
  {
    LOG.warn("Patch data invalid: " + ex.getMessage());
    List<ApiError> errors = Collections.singletonList(new ApiError("patch", ex.getMessage()));
    return new ResponseEntity<ApiErrorResponse>(new ApiErrorResponse(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex)
  {
    LOG.warn("Request body could not be read: {}", ex.getMessage());
    List<ApiError> errors = Collections.singletonList(new ApiError("requestBody", "Malformed"));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(errors));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(Exception ex)
  {
    LOG.warn("Unhandled exception: " + ex.getMessage());
    List<ApiError> errors = Collections.singletonList(new ApiError("unhandled", ex.getMessage()));
    return new ResponseEntity<ApiErrorResponse>(new ApiErrorResponse(errors), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ApiErrorResponse> mapToApiErrorResponse(BindingResult bindingResult)
  {
    List<ApiError> errors = new ArrayList<>();

    bindingResult.getAllErrors().forEach(error -> {
      String fieldName = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();

      String errorCode = error.getDefaultMessage();
      errors.add(new ApiError(fieldName, errorCode));
    });

    return ResponseEntity.badRequest().body(new ApiErrorResponse(errors));
  }
}
