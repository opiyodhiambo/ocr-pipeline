package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.model.DataModel
import com.adventure.ocrpipeline.utils.Utils
import com.fasterxml.jackson.databind.ObjectMapper
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

@Service
class TextExtractor(
    private val utils: Utils,
    @Autowired
    private val client: WebClient,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(TextExtractor::class.java)
    fun extractText(): Mono<String> {
        val pdfFile = File("src/main/resources/7f67ff37-e91f-4c2b-b2d5-05b46b88ddc6.jpeg")

        // Encode and save the file
        val mimeType = "image/jpeg"

        // Retrieve the content from the saved JSON object
        val jsonContent = utils.createRequestJson(pdfFile, mimeType)
        logger.info("Before making the request")

        // Make the POST request
        return client.post()
            .uri("/5dcd993426673b7a:process")
            .body(BodyInserters.fromValue(jsonContent))
            .retrieve()
            .bodyToMono(String::class.java)
            .flatMap { responseText ->
                val extractedDocument = objectMapper.readValue(responseText, DataModel.ExtractedDocument::class.java)
                val extractedText = extractedDocument.document.text
                Mono.just(extractedText)
            }
            .doOnSuccess{extractedText ->
//
                logger.info("Successfully extracted text: $extractedText")
            }
            .doOnError{error ->
                logger.error("Failed to extract text", error)
            }
            .log()
            .subscribeOn(Schedulers.immediate())
        }
    }

