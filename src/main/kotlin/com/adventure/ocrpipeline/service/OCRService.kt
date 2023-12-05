package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.model.DataModel
import com.adventure.ocrpipeline.model.DataModel.OCRRequested
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
    fun getIdFront(event: OCRRequested): Mono<JsonNode> {
        return textExtractor.extractIdFront(event)
    }

//    fun getData(): Mono<Map<String, Any>> {
//        val text = textExtractor.extractText()
//        return detailsParser.parseTaxDetails(text)
//    }
    fun classify(): Mono<String> {
        TODO()
    }
//    fun upload(): PutObjectResult {
//        return s3Service.uploadDocument(
//            file = File("src/main/resources/A012203309Y.pdf"),
//            key = "KE-KRA-PIN-CERTIFICATE/A012203309Y.pdf",
//            mimeType = "application/pdf"
//        )
//    }
//    fun download(){
//        return s3Service.downloadDocument("KE-KRA-PIN-CERTIFICATE/A012203309Y.pdf")
//    }
//    fun getIdBack(): Mono<Map<String, Any>> {
//        val extractedText = textExtractor.extractIdBack()
//        return detailsParser.parseIdBack(extractedText)
//    }
//    fun getIdFront(): Mono<JsonNode> {
//        return textExtractor.extractIdFront()
//    }
//    fun getPinCert(): Mono<String> {
//        return textExtractor.extractPinCert()
//    }
}

//https://tajji-kyc-documentation-bucket.ams3.digitaloceanspaces.com