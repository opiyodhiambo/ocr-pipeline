package com.adventure.ocrpipeline.model

import com.fasterxml.jackson.annotation.JsonProperty

class DataModel {
    data class ExtractedDocument(
        @JsonProperty("document")
        val document: Document
    )
    data class Document(
        val text: String
    )
}