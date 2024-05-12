package com.politicalspeech.service

import com.politicalspeech.model.Speech
import com.politicalspeech.model.Topic
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SpeechServiceTest {

    @InjectMocks
    private lateinit var speechService: SpeechService

    @Mock
    private lateinit var speechCsvReaderService: SpeechCsvReaderService

    @Test
    fun `should populate all field as null when speech list empty`() {
        val urls = listOf<String>();
        val speeches = listOf<Speech>();
        `when`(speechCsvReaderService.populateSpeechesFrom(urls)).thenReturn(speeches);

        val result = speechService.populateSpeechStatisticsResponse(urls);

        assertNull(result.mostSpeeches);
        assertNull(result.mostSecurity);
        assertNull(result.leastWordy);
    }

    @Test
    fun `should populate most speeches by certain year`() {
        val urls = listOf<String>();
        val speeches = listOf(
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-01", 200),
            Speech("Bob", Topic.HOMELAND_SECURITY, "2013-01-02", 300),
            Speech("Charlie", Topic.HOMELAND_SECURITY, "2013-01-03", 150),
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-04", 250),
            Speech("Bob", Topic.EDUCATION_POLICY, "2013-01-05", 350)
        )

        `when`(speechCsvReaderService.populateSpeechesFrom(urls)).thenReturn(speeches);

        val result = speechService.populateSpeechStatisticsResponse(urls);

        assertEquals("Bob", result.mostSpeeches)
    }


    @Test
    fun `returns mostSpeeches null, if there is no speeches in certain year`() {
        val urls = listOf<String>();
        val speeches = listOf(
            Speech("Alice", Topic.EDUCATION_POLICY, "2014-01-01", 200),
            Speech("Bob", Topic.HOMELAND_SECURITY, "2014-01-02", 300),
            Speech("Charlie", Topic.HOMELAND_SECURITY, "2014-01-03", 150),
            Speech("Alice", Topic.EDUCATION_POLICY, "2014-01-04", 250),
            Speech("Bob", Topic.EDUCATION_POLICY, "2014-01-05", 350)
        )

        `when`(speechCsvReaderService.populateSpeechesFrom(urls)).thenReturn(speeches);

        val result = speechService.populateSpeechStatisticsResponse(urls);

        assertNull(result.mostSpeeches)
    }

    @Test
    fun `should return null when one more most speeches speaker exist in certain year`() {
        val urls = listOf<String>();
        val speeches = listOf(
            Speech("Charlie", Topic.HOMELAND_SECURITY, "2013-01-03", 650),
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-01", 400),
            Speech("Bob", Topic.HOMELAND_SECURITY, "2013-01-02", 300),
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-04", 250),
            Speech("Bob", Topic.EDUCATION_POLICY, "2013-01-05", 300)
        )

        `when`(speechCsvReaderService.populateSpeechesFrom(urls)).thenReturn(speeches);

        val result = speechService.populateSpeechStatisticsResponse(urls);

        assertNull(result.mostSpeeches)
    }

    @Test
    fun `should populate least wordy`() {
        val urls = listOf<String>();
        val speeches = listOf(
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-01", 200),
            Speech("Bob", Topic.HOMELAND_SECURITY, "2013-01-02", 300),
            Speech("Charlie", Topic.HOMELAND_SECURITY, "2013-01-03", 150),
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-04", 250),
            Speech("Bob", Topic.EDUCATION_POLICY, "2013-01-05", 350)
        )

        `when`(speechCsvReaderService.populateSpeechesFrom(urls)).thenReturn(speeches);

        val result = speechService.populateSpeechStatisticsResponse(urls);

        assertEquals("Charlie", result.leastWordy)
    }

    @Test
    fun `should return null when one more least wordy speaker exist`() {
        val urls = listOf<String>();
        val speeches = listOf(
            Speech("Charlie", Topic.HOMELAND_SECURITY, "2013-01-03", 150),
            Speech("Bob", Topic.HOMELAND_SECURITY, "2013-01-02", 100),
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-04", 75),
            Speech("Bob", Topic.EDUCATION_POLICY, "2013-01-05", 50),
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-01", 75)
        )

        `when`(speechCsvReaderService.populateSpeechesFrom(urls)).thenReturn(speeches);

        val result = speechService.populateSpeechStatisticsResponse(urls);

        assertNull(result.leastWordy)
    }

    @Test
    fun `should populate most security`() {
        val urls = listOf<String>();
        val speeches = listOf(
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-01", 200),
            Speech("Bob", Topic.HOMELAND_SECURITY, "2013-01-02", 300),
            Speech("Charlie", Topic.HOMELAND_SECURITY, "2013-01-03", 150),
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-04", 250),
            Speech("Bob", Topic.EDUCATION_POLICY, "2013-01-05", 350)
        )

        `when`(speechCsvReaderService.populateSpeechesFrom(urls)).thenReturn(speeches);

        val result = speechService.populateSpeechStatisticsResponse(urls);

        assertEquals("Bob", result.mostSecurity)
    }

    @Test
    fun `should return null most security when not exist any speech`() {
        val urls = listOf<String>();
        val speeches = listOf(
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-01", 200),
            Speech("Bob", Topic.COAL_SUBSIDIES, "2013-01-02", 300),
            Speech("Charlie", Topic.COAL_SUBSIDIES, "2013-01-03", 150),
            Speech("Alice", Topic.EDUCATION_POLICY, "2013-01-04", 250),
            Speech("Bob", Topic.EDUCATION_POLICY, "2013-01-05", 350)
        )

        `when`(speechCsvReaderService.populateSpeechesFrom(urls)).thenReturn(speeches);

        val result = speechService.populateSpeechStatisticsResponse(urls);
        assertNull(result.mostSecurity)
    }

    @Test
    fun `should return null when one more speaker exist on security topic`() {
        val urls = listOf<String>();
        val speeches = listOf(
            Speech("Bob", Topic.HOMELAND_SECURITY, "2013-01-02", 100),
            Speech("Alice", Topic.HOMELAND_SECURITY, "2013-01-01", 200),
            Speech("Charlie", Topic.HOMELAND_SECURITY, "2013-01-03", 450),
            Speech("Alice", Topic.HOMELAND_SECURITY, "2013-01-04", 250),
            Speech("Bob", Topic.HOMELAND_SECURITY, "2013-01-05", 350)
        )

        `when`(speechCsvReaderService.populateSpeechesFrom(urls)).thenReturn(speeches);

        val result = speechService.populateSpeechStatisticsResponse(urls);
        assertNull(result.mostSecurity)
    }
}