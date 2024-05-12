package com.politicalspeech.service

import com.politicalspeech.exception.CsvFormatException
import com.politicalspeech.exception.CsvNotFoundException
import com.politicalspeech.model.Speech
import com.politicalspeech.model.Topic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestTemplate
import org.mockito.Mockito.`when`
import org.junit.jupiter.api.assertThrows
import org.springframework.web.client.RestClientException

@ExtendWith(MockitoExtension::class)
class SpeechCsvReaderServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @InjectMocks
    private lateinit var speechCsvReaderService: SpeechCsvReaderService

    @Test
    fun `should fetch and parse CSV successfully`() {
        val url = "http://example.com/speech.csv"
        val csvData = "Speaker;Topic;Date;Words\nJohn Doe;education policy;2024-05-12;100"
        val expectedSpeech = Speech("John Doe", Topic.EDUCATION_POLICY, "2024-05-12", 100)
        `when`(restTemplate.getForObject(url, String::class.java)).thenReturn(csvData)

        val result = speechCsvReaderService.populateSpeechesFrom(listOf(url))

        assert(result.size == 1)
        assert(result[0] == expectedSpeech)
    }

    @Test
    fun `should throw CsvNotFoundException when CSV is not found`() {
        val url = "http://example.com/nonexistent.csv"
        `when`(restTemplate.getForObject(url, String::class.java)).thenReturn(null)

        val exception = assertThrows<CsvNotFoundException> {
            speechCsvReaderService.populateSpeechesFrom(listOf(url))
        }

        assert(exception.message!!.contains("CSV file not found at URL: $url"))
    }

    @Test
    fun `should throw CsvNotFoundException when RestClientException is thrown`() {
        val url = "http://example.com/failing.csv"
        `when`(restTemplate.getForObject(url, String::class.java)).thenThrow(RestClientException::class.java)

        val exception = assertThrows<CsvNotFoundException> {
            speechCsvReaderService.populateSpeechesFrom(listOf(url))
        }

        assert(exception.message!!.contains("CSV file not found at URL: $url"))
    }

    @Test
    fun `should throw CsvFormatException when CSV is malformed`() {
        val url = "http://example.com/bad.csv"
        val malformedCsv = "Speaker,Topic,Date,Words\nJohn Doe,Politics,2022-01-01,100"
        `when`(restTemplate.getForObject(url, String::class.java)).thenReturn(malformedCsv)

        val exception = assertThrows<CsvFormatException> {
            speechCsvReaderService.populateSpeechesFrom(listOf(url))
        }

        assert(exception.message!!.contains("Failed to parse CSV due to malformed format"))
    }

    @Test
    fun `should throw CsvFormatException when CSV is not contain certain headers`() {
        val url = "http://example.com/bad.csv"
        val malformedCsv = "no header"
        `when`(restTemplate.getForObject(url, String::class.java)).thenReturn(malformedCsv)

        val exception = assertThrows<CsvFormatException> {
            speechCsvReaderService.populateSpeechesFrom(listOf(url))
        }

        assertEquals("CSV must include headers: speaker, topic, date, words" ,exception.message)
    }
}