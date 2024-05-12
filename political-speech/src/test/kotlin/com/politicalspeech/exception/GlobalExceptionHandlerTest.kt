package com.politicalspeech.exception

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.ServletWebRequest

@ExtendWith(MockitoExtension::class)
class GlobalExceptionHandlerTest {
    @InjectMocks
    private lateinit var globalExceptionHandler: GlobalExceptionHandler

    @Test
    fun `should return bad request for CsvFormatException`() {
        val exception = CsvFormatException("Malformed CSV format")

        val response = globalExceptionHandler.handleCsvFormatException(exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(exception.message, response.body!!.message)
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.body!!.status)
    }

    @Test
    fun `should return bad request for CsvNotFoundException`() {
        val exception = CsvNotFoundException("CSV file not found")

        val response = globalExceptionHandler.handleCsvNotFoundException(exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(exception.message, response.body!!.message)
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.body!!.status)
    }

    @Test
    fun `should return internal server error for general exceptions`() {
        val exception = Exception("General error")

        val response = globalExceptionHandler.handleException(exception)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("Something bad wrong please reach the system administrator", response.body!!.message)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.body!!.status)
    }
}