/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package com.matchmetrics.controller.controller_advice;

import com.matchmetrics.entity.ApiErrorResponse;
import com.matchmetrics.exception.*;
import com.matchmetrics.security.exception.ActionRestrictedException;
import com.matchmetrics.security.exception.EmailTakenException;
import com.matchmetrics.security.exception.UserDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FieldDoesNotExistException.class)
    public ResponseEntity<ApiErrorResponse> handleFieldDoesNotExistException
            (FieldDoesNotExistException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateConversionException.class)
    public ResponseEntity<ApiErrorResponse> handleDateConversionException
            (DateConversionException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDataException
            (InvalidDataException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MatchDoesNotExistException.class)
    public ResponseEntity<ApiErrorResponse> handleMatchDoesNotExistException
            (MatchDoesNotExistException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamDoesNotExistException.class)
    public ResponseEntity<ApiErrorResponse> handleTeamDoesNotExistException
            (TeamDoesNotExistException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleTeamAlreadyExistsException
            (TeamAlreadyExistsException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProbabilityDoesNotExistException.class)
    public ResponseEntity<ApiErrorResponse> handleProbabilityDoesNotExistException
            (ProbabilityDoesNotExistException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AssociatedProbabilityException.class)
    public ResponseEntity<ApiErrorResponse> handleAssociatedProbabilityException
            (AssociatedProbabilityException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.IM_USED.value(),
                HttpStatus.IM_USED.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.IM_USED);
    }

    @ExceptionHandler(InvalidProbabilityException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidProbabilityException
            (InvalidProbabilityException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidProbabilityException
            (EmailTakenException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.IM_USED.value(),
                HttpStatus.IM_USED.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.IM_USED);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidProbabilityException
            (UserDoesNotExistException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ActionRestrictedException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidProbabilityException
            (ActionRestrictedException e, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                e.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<ApiErrorResponse> handleInvalidProbabilityException
//            (ExpiredJwtException e, WebRequest request) {
//        ApiErrorResponse errorResponse = new ApiErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.FORBIDDEN.value(),
//                HttpStatus.FORBIDDEN.getReasonPhrase(),
//                e.getMessage(),
//                ((ServletWebRequest) request).getRequest().getRequestURI()
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(UnsupportedJwtException.class)
//    public ResponseEntity<ApiErrorResponse> handleInvalidProbabilityException
//            (UnsupportedJwtException e, WebRequest request) {
//        ApiErrorResponse errorResponse = new ApiErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.FORBIDDEN.value(),
//                HttpStatus.FORBIDDEN.getReasonPhrase(),
//                e.getMessage(),
//                ((ServletWebRequest) request).getRequest().getRequestURI()
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(MalformedJwtException.class)
//    public ResponseEntity<ApiErrorResponse> handleInvalidProbabilityException
//            (MalformedJwtException e, WebRequest request) {
//        ApiErrorResponse errorResponse = new ApiErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.FORBIDDEN.value(),
//                HttpStatus.FORBIDDEN.getReasonPhrase(),
//                e.getMessage(),
//                ((ServletWebRequest) request).getRequest().getRequestURI()
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ApiErrorResponse> handleInvalidProbabilityException
//            (IllegalArgumentException e, WebRequest request) {
//        ApiErrorResponse errorResponse = new ApiErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.FORBIDDEN.value(),
//                HttpStatus.FORBIDDEN.getReasonPhrase(),
//                e.getMessage(),
//                ((ServletWebRequest) request).getRequest().getRequestURI()
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }
}

