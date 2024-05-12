package com.politicalspeech.service

import com.politicalspeech.model.Speech
import com.politicalspeech.model.SpeechStatisticsResponse
import com.politicalspeech.model.Topic
import org.springframework.stereotype.Service

@Service
class SpeechService(val speechCsvReaderService: SpeechCsvReaderService) {

    fun populateSpeechStatisticsResponse(urls: List<String>): SpeechStatisticsResponse {
        val speeches = speechCsvReaderService.populateSpeechesFrom(urls);
        if(speeches.isEmpty()) {
            return SpeechStatisticsResponse(null, null, null)
        }
        val mostSpeeches = findByMostSpeechesFromSpecialYear(speeches);
        val mostSecurity = findMostSpeechesOnHomelandSecurity(speeches);
        val leastWordy = findFewestWordsOverall(speeches);

        return SpeechStatisticsResponse(mostSpeeches, mostSecurity, leastWordy)
    }

    private fun findByMostSpeechesFromSpecialYear(speeches: List<Speech>, targetYear: Int = 2013): String? {
        val speechesInCertainYear = speeches.filter { it.date.contains(targetYear.toString()) }
        val speakerTotalWordCountMap = speechesInCertainYear
            .groupBy(Speech::speaker)
            .mapValues { it.value.sumOf(Speech::words) }

        val maxWordCount = speakerTotalWordCountMap.maxByOrNull { it.value }?.value

        val speakersWithMaxWords = speakerTotalWordCountMap.filter { it.value == maxWordCount }.keys

        return if (speakersWithMaxWords.size > 1) {
            null
        } else {
            speakersWithMaxWords.firstOrNull()
        }
    }

    private fun findFewestWordsOverall(speeches: List<Speech>): String? {
        val speakerTotalWordCountMap = speeches
            .groupBy(Speech::speaker)
            .mapValues { it.value.sumOf(Speech::words) }

        val minWordCount = speakerTotalWordCountMap.minByOrNull { it.value }?.value

        val speakersWithMinWords = speakerTotalWordCountMap.filter { it.value == minWordCount }.keys

        return if (speakersWithMinWords.size > 1) {
            null
        } else {
            speakersWithMinWords.firstOrNull()
        }
    }

    private fun findMostSpeechesOnHomelandSecurity(speeches: List<Speech>, topic: Topic = Topic.HOMELAND_SECURITY): String? {
        val speechOnSecurityTopic = speeches.filter { it.topic == topic }
        val speakerTotalWordCountMap = speechOnSecurityTopic
            .groupBy(Speech::speaker)
            .mapValues { it.value.sumOf(Speech::words) }

        val maxWordCount = speakerTotalWordCountMap.maxByOrNull { it.value }?.value

        val speakersWithMaxWords = speakerTotalWordCountMap.filter { it.value == maxWordCount }.keys

        return if (speakersWithMaxWords.size > 1) {
            null
        } else {
            speakersWithMaxWords.firstOrNull()
        }
    }

}