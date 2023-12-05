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
}