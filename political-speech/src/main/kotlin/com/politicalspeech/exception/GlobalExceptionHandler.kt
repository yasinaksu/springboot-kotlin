package com.politicalspeech.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CsvFormatException::class)
    fun handleCsvFormatException(ex: CsvFormatException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity(
            ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.message),
            HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(CsvNotFoundException::class)
    fun handleCsvNotFoundException(ex: CsvNotFoundException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity(
            ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.message),
            HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ExceptionResponse> {
        return ResponseEntity(
            ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something bad wrong please reach the system administrator"),
            HttpStatus.INTERNAL_SERVER_ERROR)
    }
}