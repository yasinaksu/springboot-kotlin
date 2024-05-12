package com.politicalspeech.service

import com.politicalspeech.exception.CsvFormatException
import com.politicalspeech.exception.CsvNotFoundException
import com.politicalspeech.model.Speech
import com.politicalspeech.model.Topic
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.io.BufferedReader
import java.io.StringReader

@Service
class SpeechCsvReaderService(val restTemplate: RestTemplate) {

    fun populateSpeechesFrom(urls: List<String>): List<Speech> =
        urls.flatMap { url -> fetchAndParseCsv(url) }

    private fun fetchCsvData(url: String): String =
        try {
            restTemplate.getForObject(url, String::class.java)
                ?: throw CsvNotFoundException("CSV file not found at URL: $url")
        } catch (ex: RestClientException) {
            throw CsvNotFoundException("CSV file not found at URL: $url")
        }

    private fun parseCsvData(csvData: String): List<Speech> {
        try {
            return BufferedReader(StringReader(csvData)).use { reader ->
                createCsvParser(reader).use { csvParser ->
                    csvParser.records.map { record ->
                        createSpeechFrom(record)
                    }
                }
            }
        } catch (e: Exception) {
            throw CsvFormatException("Failed to parse CSV due to malformed format please use semicolon ';' as delimiter ${e.message}")
        }
    }

    private fun createCsvParser(reader: BufferedReader): CSVParser =
        CSVFormat.DEFAULT.builder()
            .setHeader("Speaker", "Topic", "Date", "Words")
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .setTrim(true)
            .setDelimiter(';')
            .build()
            .parse(reader)

    private fun createSpeechFrom(record: CSVRecord): Speech =
        Speech(
            speaker = record["Speaker"],
            topic = Topic.fromValue(record["Topic"]),
            date = record["Date"],
            words = record["Words"].toInt()
        )

    private fun fetchAndParseCsv(url: String): List<Speech> {
        val csv = fetchCsvData(url)
        validateCsvHeaders(csv)
        return parseCsvData(csv)
    }

    private fun validateCsvHeaders(csv: String) {
        val requiredHeaders = listOf("speaker", "topic", "date", "words")
        val lowerCsv = csv.lowercase()

        val missingHeaders = requiredHeaders.filter { header -> !lowerCsv.contains(header) }
        if (missingHeaders.isNotEmpty()) {
            throw CsvFormatException("CSV must include headers: ${missingHeaders.joinToString(", ")}")
        }
    }
}