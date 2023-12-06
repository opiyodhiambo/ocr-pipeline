package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.model.DataModel
import com.adventure.ocrpipeline.model.DataModel.OCRRequested
import com.adventure.ocrpipeline.utils.Utils
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File

@Service
class DocumentClassifier(
    private val utils: Utils,
    private val client: WebClient,
    private val s3Service: S3Service
) {
    private val logger = LoggerFactory.getLogger(DocumentClassifier::class.java)
    private val mimeType = "image/jpeg"
    fun extractClass(event: OCRRequested): Mono<JsonNode> {
        logger.info("Classifying the document")
        val fileMono = s3Service.downloadDocument(
            event.nationalIdData.folder,
            event.nationalIdData.documentName
        )
        return fileMono.flatMap { file ->
            logger.info("Downloaded the document, $file")
            val jsonContentMono = utils.createRequestJson(Mono.just(file), mimeType)
            jsonContentMono.flatMap { jsonContent ->
                client.post()
                    .uri("/858e10cb5834b53a:process")
                    .body(BodyInserters.fromValue(jsonContent))
                    .retrieve()
                    .bodyToMono(JsonNode::class.java)
                    .flatMap { node -> Mono.just(node.get("document").get("entities")) }
                    .doOnSuccess { response ->
                        utils.processAndLogResponse(response)
                    }
                    .doOnError { error ->
                        logger.error("Failed to classifyText", error)
                    }
                    .log()
                    .subscribeOn(Schedulers.immediate())
            }
        }
    }
}