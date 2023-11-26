package com.adventure.ocrpipeline.service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OCRService(
    private val textExtractor: TextExtractor,
    private val detailsParser: DetailsParser,
    private val documentClassifier: DocumentClassifier
) {
    fun getData(): Mono<Map<String, Any>> {
        val text = textExtractor.extractText()
        return detailsParser.parseTaxDetails(text)
    }
    fun classify(): Mono<String> {
        return documentClassifier.classifyDocument()
    }
}