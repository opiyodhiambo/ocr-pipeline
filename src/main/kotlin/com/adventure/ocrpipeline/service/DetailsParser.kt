package com.adventure.ocrpipeline.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DetailsParser(
    private val objectMapper: ObjectMapper) {
    fun parseTaxDetails(extractedText: Mono<String>): Mono<Map<String, Any>> {
        return parseText(extractedText)
        }
    }
    private fun parseText(text: Mono<String>): Mono<Map<String, Any>> {
        val logger: Logger = LoggerFactory.getLogger(DetailsParser::class.java)
        return text.map { rawText ->
            val newLine = rawText.split("\n")
            val result = mapOf(
                "SerialNumber" to newLine[1].trim()
                    .split(":")[1].trim(),
                "firstName" to newLine[3].trim()
                    .split(" ")[0].trim(),
                "middleName" to newLine[3].trim()
                    .split(" ")[1].trim(),
                "lastName" to newLine[3].trim()
                    .split(" ")[2].trim(),
                "dateOfBirth" to newLine[6].trim(),
                "sex" to newLine[8].trim(),
                "districtOfBirth" to newLine[10].trim(),
                "placeOfIssue" to newLine[12].trim(),
                "dateOfIssue" to newLine[14].trim(),
                "idNumber" to newLine[17].trim()
                    .split(":")[1].trim()
            )
            logger.info(result.toString())
            result
        }

    }
