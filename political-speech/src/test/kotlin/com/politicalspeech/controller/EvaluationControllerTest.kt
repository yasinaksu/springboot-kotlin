package com.politicalspeech.controller

import com.politicalspeech.model.SpeechStatisticsResponse
import com.politicalspeech.service.SpeechService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content


@ExtendWith(SpringExtension::class)
@WebMvcTest(EvaluationController::class)
class EvaluationControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var speechService: SpeechService

    @Test
    fun `should return OK and SpeechStatisticsResponse when valid URL list is provided`() {
        val urlList = listOf("http://example.com/speech1.csv", "http://example.com/speech2.csv")
        val expectedResponse = SpeechStatisticsResponse("example", "example", "example")

        `when`(speechService.populateSpeechStatisticsResponse(urlList)).thenReturn(expectedResponse)

        mockMvc.perform(get("/api/evaluation")
            .param("url", *urlList.toTypedArray())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().json("{\"mostSpeeches\":\"example\",\"mostSecurity\":\"example\",\"leastWordy\":\"example\"}"))
    }
}