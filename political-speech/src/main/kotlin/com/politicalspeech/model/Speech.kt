package com.politicalspeech.model

import java.time.LocalDate

data class Speech (
    val speaker: String,
    val topic: Topic,
    val date: String,
    val words: Int
)