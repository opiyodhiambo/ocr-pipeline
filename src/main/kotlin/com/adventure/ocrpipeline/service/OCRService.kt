package com.adventure.ocrpipeline.service

import com.amazonaws.services.s3.model.PutObjectResult
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File

@Service
class OCRService(
    private val textExtractor: TextExtractor,
    private val detailsParser: DetailsParser,
    private val documentClassifier: DocumentClassifier,
    private val s3Service: S3Service
) {
    fun getData(): Mono<Map<String, Any>> {
        val text = textExtractor.extractText()
        return detailsParser.parseTaxDetails(text)
    }
    fun classify(): Mono<String> {
        return documentClassifier.classifyDocument()
    }
    fun upload(): PutObjectResult {
        return s3Service.uploadDocument(
            file = File("src/main/resources/A012203309Y.pdf"),
            key = "KE-KRA-PIN-CERTIFICATE/A012203309Y.pdf",
            mimeType = "application/pdf"
        )
    }
}