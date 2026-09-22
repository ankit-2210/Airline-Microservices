package com.locationservice.exception;

import com.airlineportal.exception.ResourceAlreadyExistsException;
import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.exception.UnauthorizedException;
import com.airlineportal.exception.response.ApiErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, String path, Map<String, String> validationErrors){
        ApiErrorResponse response = new ApiErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, path, validationErrors);
        return ResponseEntity.status(status).body(response);
    }

    // 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    // 409 - Resource Already Exists
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleAlreadyExists(ResourceAlreadyExistsException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null);
    }

    // 401 - Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI(), null);
    }

    // 401 - Bad Credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password", request.getRequestURI(), null);
    }

    // 403 - Access Denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.FORBIDDEN, "You don't have permission", request.getRequestURI(), null);
    }

    // 400 - Illegal Argument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    // 400 - @Valid RequestBody Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request){
        Map<String, String> errors =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                                (oldValue, newValue) -> oldValue
                        ));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", request.getRequestURI(), errors);
    }

    // 400 - @RequestParam / @PathVariable Validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request){
        Map<String, String> errors =
                ex.getConstraintViolations()
                        .stream()
                        .collect(Collectors.toMap(
                                violation ->
                                        violation
                                                .getPropertyPath()
                                                .toString(),
                                ConstraintViolation::getMessage,
                                (oldValue, newValue) -> oldValue
                        ));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", request.getRequestURI(), errors);
    }

    // 400 - Invalid JSON / Request Body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request body", request.getRequestURI(), null);
    }

    // 400 - Invalid Parameter Type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid parameter: " + ex.getName(), request.getRequestURI(), null);
    }

    // 401 - Expired JWT
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwt(ExpiredJwtException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.UNAUTHORIZED, "Token expired", request.getRequestURI(), null);
    }

    // 401 - Invalid JWT
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handleJwt(JwtException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid token", request.getRequestURI(), null);
    }

    // 500 - Messaging / Email Error
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiErrorResponse> handleMessage(MessagingException ex, HttpServletRequest request){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Email service unavailable", request.getRequestURI(), null);
    }

    // 500 - Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest request){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", request.getRequestURI(), null);
    }
}