package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.model.DataModel
import com.adventure.ocrpipeline.model.DataModel.Document
import com.adventure.ocrpipeline.utils.Utils
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.google.api.client.json.Json
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File
import java.util.Base64
import java.util.logging.Level

@Service
class TextExtractor(
    private val utils: Utils,
    @Autowired
    private val client: WebClient,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(TextExtractor::class.java)
    fun extractText(): Mono<String> {
        val pdfFile = File("src/main/resources/b0c8e19d-ccca-4921-bf45-4819afeae148.jpeg")
        val mimeType = "image/jpeg"

        // Retrieve the content from the saved JSON object
        val jsonContent = utils.createRequestJson(pdfFile, mimeType)

        return client.post()
            .uri("/5dcd993426673b7a:process")
            .body(BodyInserters.fromValue(jsonContent))
            .retrieve()
            .bodyToMono(JsonNode::class.java)
            .flatMap { node -> Mono.just(node.get("document").get("text").asText()) }
            .doOnSuccess { textNode ->
                utils.processAndLogResponse(textNode)
            }
            .doOnError { error ->
                logger.error("Failed to extract text", error)
            }
            .log("web-client", Level.FINE)
            .subscribeOn(Schedulers.immediate())
        // Make the POST request
            }
    }