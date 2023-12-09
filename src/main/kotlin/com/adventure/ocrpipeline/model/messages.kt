package com.adventure.ocrpipeline.model

import com.adventure.ocrpipeline.model.DataModel.NationalIdData
import com.adventure.ocrpipeline.model.DataModel.OCRRequested
import com.fasterxml.jackson.databind.JsonNode
import reactor.core.publisher.Mono


sealed class Message {
    data class Event(
        val event: OCRRequested
    ): Message()
    data class ExtractedText(
        val text: Mono<JsonNode>
    ): Message()
    data class ParsedText(
        val nationalIdData: NationalIdData
    ): Message()
}