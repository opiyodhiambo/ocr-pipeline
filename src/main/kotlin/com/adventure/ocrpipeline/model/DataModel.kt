package com.adventure.ocrpipeline.model

import akka.actor.typed.ActorRef
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import reactor.core.publisher.Mono


class DataModel {
    data class ExtractedDocument(
        @JsonProperty("document")
        val document: Document
    )

    data class Document(
        val text: String
    )
    data class DocumentDetails(
        val serialNumber: String,
        val fullName: String
    )
    data class OCRRequested(
        val nationalIdData: DocumentData
    )
    data class DocumentData(
        val bucket: String,
        val folder: String,
        val documentName: String
    )
    data class NationalIdData(
        val idNumber : String,
        val serialNumber : String,
        val firstName : String,
        val middleName : String,
        val lastName : String,
        val districtOfBirth : String,
        val placeOfIssue : String,
        val dateOfIssue : String,
        val dateOfBirth : String,
        val sex : Gender,
        val nationality : Nationality
    )
    enum class Gender(val value: String){
        UNDEFINED("UNDEFINED"),
        MALE("MALE"),
        FEMALE("FEMALE")
    }
    enum class Nationality(val value: String){
        KE("KE")
    }
    data class DocumentExtracted(
        val data: NationalIdData
    )

}