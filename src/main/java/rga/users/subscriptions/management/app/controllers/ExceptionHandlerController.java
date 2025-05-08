package rga.users.subscriptions.management.app.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rga.users.subscriptions.management.app.dtos.ResponseMessageDto;
import rga.users.subscriptions.management.app.exceptions.*;

@Hidden
@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class ExceptionHandlerController {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<ResponseMessageDto> handleNotFoundException(HttpServletRequest request, NotFoundException e) {
        logError(e);
        return new ResponseEntity<>(new ResponseMessageDto(request.getRequestURI(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ExpiredJwtException.class,
            UnsupportedJwtException.class,
            MalformedJwtException.class})
    @ResponseBody
    protected ResponseEntity<ResponseMessageDto> handleUnauthorizedException(HttpServletRequest request, RuntimeException e) {
        logError(e);
        return new ResponseEntity<>(new ResponseMessageDto(request.getRequestURI(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler({InvalidDataException.class,
            UserAlreadyExistentException.class,
            BadRequestException.class,
            HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseEntity<ResponseMessageDto> handleBadRequest(HttpServletRequest request, RuntimeException e) {
        logError(e);
        return new ResponseEntity<>(new ResponseMessageDto(request.getRequestURI(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessForbiddenException.class)
    @ResponseBody
    protected ResponseEntity<ResponseMessageDto> handleAccessForbiddenException(HttpServletRequest request, AccessForbiddenException e) {
        logError(e);
        return new ResponseEntity<>(new ResponseMessageDto(request.getRequestURI(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseBody
    public ResponseEntity<ResponseMessageDto> handleServiceUnavailableException(HttpServletRequest request, ServiceUnavailableException e) {
        logError(e);
        return new ResponseEntity<>(new ResponseMessageDto(request.getRequestURI(), e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    private void logError(RuntimeException e) {
        log.error("Source:{}. Message:{}.", messageSource, ExceptionUtils.getMessage(e));
    }
    
}
