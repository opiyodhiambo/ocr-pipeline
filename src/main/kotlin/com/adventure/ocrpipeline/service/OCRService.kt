package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.model.DataModel
import com.adventure.ocrpipeline.model.DataModel.*
import com.adventure.ocrpipeline.utils.Utils
import com.fasterxml.jackson.databind.JsonNode
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.GenericEventMessage
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File

@Service
class OCRService(
    private val textExtractor: TextExtractor,
    private val detailsParser: DetailsParser,
    private val eventBus: EventBus,
    private val documentClassifier: DocumentClassifier,
    private val utils: Utils
) {
    fun getIdFront(event: OCRRequested): Mono<NationalIdData> {
        val extractedText = textExtractor.extractIdFront(event)
        return detailsParser.parseIdFront(extractedText)
            .doOnSuccess { nationalIdData ->
                val documentExtractedEvent = DocumentExtracted(data = nationalIdData)
                eventBus.publish(GenericEventMessage.asEventMessage<DocumentExtracted>(documentExtractedEvent))
            }
    }
    fun validateDocument(event: OCRRequested): Mono<String> {
        val documentClass =  documentClassifier.extractClass(event)
        return utils.classifyDocument(documentClass)
    }
}