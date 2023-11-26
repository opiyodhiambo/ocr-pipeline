package com.adventure.ocrpipeline.service

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
    private val client: WebClient
) {
    private val logger = LoggerFactory.getLogger(DocumentClassifier::class.java)
    fun classifyDocument(): Mono<String> {
        val file = File("src/main/resources/A012203309Y.pdf")
        val mimeType = "application/pdf"

        // Retrieving the Json object
        val jsonContent = utils.createRequestJson(file, mimeType)

        return client.post()
            .uri("/858e10cb5834b53a:process")
            .body(BodyInserters.fromValue(jsonContent))
            .retrieve()
            .bodyToMono(String::class.java)
            .doOnSuccess { validity ->
                utils.processAndLogResponse(validity)
            }
            .doOnError { error ->
                logger.error("Failed to classify text", error)
            }
            .log()
            .subscribeOn(Schedulers.immediate())

    }
}