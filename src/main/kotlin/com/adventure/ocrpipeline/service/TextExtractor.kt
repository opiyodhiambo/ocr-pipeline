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
    private val imageMimeType = "image/jpeg"
    private val pdfMimeType = "application/pdf"
//    fun extractText(): Mono<String> {
//        val pdfFile = File("src/main/resources/")
//
//        // Retrieve the content from the saved JSON object
//        val jsonContent = utils.createRequestJson(pdfFile, imageMimeType)
//
//        return client.post()
//            .uri("/5dcd993426673b7a:process")
//            .body(BodyInserters.fromValue(jsonContent))
//            .retrieve()
//            .bodyToMono(JsonNode::class.java)
//            .flatMap { node -> Mono.just(node.get("document").get("text").asText()) }
//            .doOnSuccess { textNode ->
//                utils.processAndLogResponse(textNode)
//            }
//            .doOnError { error ->
//                logger.error("Failed to extract text", error)
//            }
//            .log("web-client", Level.FINE)
//            .subscribeOn(Schedulers.immediate())
//        // Make the POST request
//            }

    fun extractIdFront(): Mono<String> {
        // The document and mimetype
        val idFront = File("src/main/resources/data/front.jpeg")
        // The content
        val jsonContent = utils.createRequestJson(idFront, imageMimeType)
        return client.post()
            .uri("/20ce4708aff5662d:process")
            .body(BodyInserters.fromValue(jsonContent))
            .retrieve()
            .bodyToMono(String::class.java)
            .flatMap { node -> Mono.just(node) }
            .doOnSuccess { text ->
                utils.processAndLogResponse(text)
            }
            .doOnError { error ->
                logger.error("Failed to extract text", error)
            }
            .log()
            .subscribeOn(Schedulers.immediate())
    }
    fun extractIdBack(): Mono<JsonNode> {
        // The document and mimetype
        val idBack = File("src/main/resources/data/back.jpeg")
        // The content
        val jsonContent = utils.createRequestJson(idBack, imageMimeType)
        return client.post()
            .uri("/53f494082d12c245:process")
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
            .log()
            .subscribeOn(Schedulers.immediate())
    }
    fun extractPinCert(): Mono<String> {
        // The document and mimetype
        val pinCert = File("src/main/resources/data/PinCert.pdf")
        // The content
        val jsonContent = utils.createRequestJson(pinCert, pdfMimeType)
        return client.post()
            .uri("/2ea8824500626fb7:process")
            .body(BodyInserters.fromValue(jsonContent))
            .retrieve()
            .bodyToMono(String::class.java)
            .flatMap { node -> Mono.just(node) }
            .doOnSuccess { text ->
                utils.processAndLogResponse(text)
            }
            .doOnError { error ->
                logger.error("Failed to extract text", error)
            }
            .log()
            .subscribeOn(Schedulers.immediate())
    }

}