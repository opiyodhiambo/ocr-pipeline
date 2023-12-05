package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.utils.Utils
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.*

@Service
class DetailsParser(
    private val objectMapper: ObjectMapper,
    private val utils: Utils) {
    fun parseTaxDetails(extractedText: Mono<String>): Mono<Map<String, Any>> {
//        return parseText(extractedText)
        TODO()
        }
    fun parseIdBack(text: Mono<JsonNode>): Mono<Map<String, Any>> {
        return text.flatMap { entitiesNode ->
            val parsedMap = parseJsonNodeToMap(entitiesNode)
            utils.processAndLogResponse(parsedMap)
            Mono.just(parsedMap)
                .subscribeOn(Schedulers.immediate())
        }
    }
//    fun parseIdFront(text: Mono<JsonNode>): Mono<Map<String, Any>> {
//        return text.flatMap { entitiesNode ->
//            val parsedM
//        }
//    }
    private fun parseJsonNodeToMap(jsonNode: JsonNode): Map<String, Any> {
        val objectMapper = jacksonObjectMapper()
        val valueTypeRef = object : TypeReference<List<Map<String, Any>>>() {}
        val listOfMaps: List<Map<String, Any>> = objectMapper.convertValue(jsonNode, valueTypeRef)
        return listOfMaps.associate { it["type"].toString() to it["mentionText"].toString().lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
        }
    }

//    private fun parseText(text: Mono<String>): Mono<Map<String, Any>> {
//        val logger: Logger = LoggerFactory.getLogger(DetailsParser::class.java)
//        return text.map { rawText ->
//            val newLine = rawText.split("\n")
//            val result = mapOf(
//                "SerialNumber" to newLine[1].trim()
//                    .split(":")[1].trim(),
//                "firstName" to newLine[3].trim()
//                    .split(" ")[0].trim(),
//                "middleName" to newLine[3].trim()
//                    .split(" ")[1].trim(),
//                "lastName" to newLine[3].trim()
//                    .split(" ")[2].trim(),
//                "dateOfBirth" to newLine[6].trim(),
//                "sex" to newLine[8].trim(),
//                "districtOfBirth" to newLine[10].trim(),
//                "placeOfIssue" to newLine[12].trim(),
//                "dateOfIssue" to newLine[14].trim(),
//                "idNumber" to newLine[17].trim()
//                    .split(":")[1].trim()
//            )
//            logger.info(result.toString())
//            result
//        }



