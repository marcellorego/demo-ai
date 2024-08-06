package com.softway.ai.web.advice;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {
    public static final String AI_CLIENT_RAISED_EXCEPTION = "AI client raised exception";

    @ExceptionHandler(IOException.class)
    ProblemDetail handleOpenAiHttpException(IOException ex) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle(AI_CLIENT_RAISED_EXCEPTION);
        return problemDetail;
    }
}