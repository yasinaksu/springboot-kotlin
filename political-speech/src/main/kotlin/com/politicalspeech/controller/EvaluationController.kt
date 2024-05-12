package com.politicalspeech.controller

import com.politicalspeech.model.SpeechStatisticsResponse
import com.politicalspeech.service.SpeechService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class EvaluationController(val speechService: SpeechService) {

    @GetMapping("evaluation")
    fun getSpeechStatistics(@RequestParam url: List<String>): ResponseEntity<SpeechStatisticsResponse> {
        return ResponseEntity.ok(speechService.populateSpeechStatisticsResponse(url))
    }
}