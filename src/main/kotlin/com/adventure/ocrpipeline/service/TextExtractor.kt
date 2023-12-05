package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.model.DataModel.OCRRequested
import com.adventure.ocrpipeline.utils.Utils
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.axonframework.eventhandling.EventHandler
import org.axonframework.messaging.annotation.MetaDataValue
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class TextExtractor(
    private val utils: Utils,
    @Autowired
    private val client: WebClient,
    private val s3Service: S3Service
) {
    private val logger = LoggerFactory.getLogger(TextExtractor::class.java)
    private val imageMimeType = "image/jpeg"

    fun extractIdFront(event: OCRRequested): Mono<JsonNode> {
        logger.info("Extracting OCRRequested event")
        val documentMono = s3Service.downloadDocument(event.nationalIdData.folder,
            event.nationalIdData.documentName)
        return documentMono.flatMap { document ->
            logger.info("Downloaded document, $document")
            val jsonContentMono = utils.createRequestJson(Mono.just(document), imageMimeType)
            jsonContentMono.flatMap { jsonContent ->
                client.post()
                    .uri("/20ce4708aff5662d:process")
                    .body(BodyInserters.fromValue(jsonContent))
                    .retrieve()
                    .bodyToMono(JsonNode::class.java)
                    .flatMap { node -> Mono.just(node.get("document").get("entities")) }
                    .doOnSuccess { text ->
                        utils.processAndLogResponse(text)
                    }
                    .doOnError { error ->
                        logger.error("Failed to extract text", error)
                    }
                    .doOnError { error ->
                        logger.error("Error in HTTP request", error)
                    }
                    .log()
                    .subscribeOn(Schedulers.boundedElastic())
            }
        }
    }
}
