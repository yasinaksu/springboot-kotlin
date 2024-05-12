package com.politicalspeech

import com.politicalspeech.model.Topic
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PoliticalSpeechApplication

fun main(args: Array<String>) {
	runApplication<PoliticalSpeechApplication>(*args)
}
