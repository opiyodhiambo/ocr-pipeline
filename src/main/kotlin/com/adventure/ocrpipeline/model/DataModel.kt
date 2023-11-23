package com.adventure.ocrpipeline.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.api.client.json.Json

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
}