package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.model.DataModel
import com.adventure.ocrpipeline.model.DataModel.NationalIdData
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
import java.time.LocalDate
import java.util.*

@Service
class DetailsParser(
    private val objectMapper: ObjectMapper,
    private val utils: Utils) {
    fun parseIdFront(text: Mono<JsonNode>): Mono<NationalIdData> {
        return text.flatMap { entitiesNode ->
            val parsedMap = parseJsonNodeToMap(entitiesNode)
            val fullNameParts = parsedMap["full-names"].toString().split(" ")
            val idObject = NationalIdData(
                idNumber = parsedMap["id-no"] as String,
                serialNumber = parsedMap["serial-number"] as String,
                firstName = fullNameParts.getOrElse(0) { "" },
                middleName = fullNameParts.getOrElse(1) { "" }
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                lastName = fullNameParts.getOrElse(2) { "" }
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                districtOfBirth = parsedMap["district-ob"] as String,
                placeOfIssue = parsedMap["poi"] as String,
                dateOfIssue = parsedMap["doi"] as String,
                dateOfBirth = parsedMap["dob"] as String,
                sex = DataModel.Gender.valueOf(parsedMap["sex"].toString().uppercase(Locale.getDefault())),
                nationality = DataModel.Nationality.KE

            )
            utils.processAndLogResponse(idObject)
            Mono.just(idObject)
                .subscribeOn(Schedulers.immediate())
        }
    }
    private fun parseJsonNodeToMap(jsonNode: JsonNode): Map<String, Any> {
        val objectMapper = jacksonObjectMapper()
        val valueTypeRef = object : TypeReference<List<Map<String, Any>>>() {}
        val listOfMaps: List<Map<String, Any>> = objectMapper.convertValue(jsonNode, valueTypeRef)
        return listOfMaps.associate { it["type"].toString() to it["mentionText"].toString().lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }
}


