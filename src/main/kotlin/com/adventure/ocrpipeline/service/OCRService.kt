package com.adventure.ocrpipeline.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OCRService(
    private val textExtractor: TextExtractor,
    private val detailsParser: DetailsParser
) {
    fun getData(): Mono<Map<String, Any>> {
        val text = textExtractor.extractText()
        return detailsParser.parseTaxDetails(text)
    }
}