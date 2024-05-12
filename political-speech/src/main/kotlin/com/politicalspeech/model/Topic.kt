package com.politicalspeech.model

enum class Topic(val value: String) {
    EDUCATION_POLICY("education policy"),
    COAL_SUBSIDIES("coal subsidies"),
    HOMELAND_SECURITY("homeland security");

    companion object {
        fun fromValue(value: String): Topic = when(value.lowercase()) {
            "education policy" -> EDUCATION_POLICY
            "coal subsidies" -> COAL_SUBSIDIES
            "homeland security" -> HOMELAND_SECURITY
            else -> throw IllegalArgumentException("There is no topic such as $value")
        }
    }
}