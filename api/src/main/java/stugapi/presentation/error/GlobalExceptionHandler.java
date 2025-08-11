package stugapi.presentation.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)  // Explicitly set HTTP status
  @ResponseBody  // Explicitly mark for response body serialization
  public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
    log.error("Entity not found exception occurred: {}", ex.getMessage());

    return createErrorResponse(
      HttpStatus.NO_CONTENT,
      "Resource not found",
      ex.getMessage()
    );
  }

  @ExceptionHandler(InvalidArendeIdException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)  // Explicitly set HTTP status
  @ResponseBody
  public ErrorResponse handleInvalidArendeIdException(InvalidArendeIdException ex, WebRequest request) {
    return createErrorResponse(
      HttpStatus.BAD_REQUEST,
      ex.getMessage(),
      request.getDescription(false)
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)  // Explicitly set HTTP status
  @ResponseBody
  public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
    String errors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(error -> error.getField() + ": " + error.getDefaultMessage())
      .collect(Collectors.joining(", "));

    return createErrorResponse(
      HttpStatus.BAD_REQUEST,
      "Validation failed",
      errors
    );
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)  // Explicitly set HTTP status
  @ResponseBody
  public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
    return createErrorResponse(
      HttpStatus.BAD_REQUEST,
      "Validation failed",
      ex.getMessage()
    );
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)  // Explicitly set HTTP status
  @ResponseBody
  public ErrorResponse handleAuthenticationException(AuthenticationException ex, WebRequest request) {
    return createErrorResponse(
      HttpStatus.UNAUTHORIZED,
      "Authentication failed",
      ex.getMessage()
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)  // Explicitly set HTTP status
  @ResponseBody
  public ErrorResponse handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    return createErrorResponse(
      HttpStatus.FORBIDDEN,
      "Access denied",
      ex.getMessage()
    );
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // Explicitly set HTTP status
  @ResponseBody
  public ErrorResponse handleAllUncaughtException(Exception ex, WebRequest request) {
    return createErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "An unexpected error occurred",
      ex.getMessage()
    );
  }

  private ErrorResponse createErrorResponse(HttpStatus status, String message,  String details) {
    return new ErrorResponse(
      LocalDateTime.now(),
      status.value(),
      status.getReasonPhrase(),
      message,
      details
    );
  }
}
