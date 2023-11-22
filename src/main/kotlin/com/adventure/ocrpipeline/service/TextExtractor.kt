package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.utils.Utils
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File

@Service
class TextExtractor(
    private val utils: Utils,
    @Autowired
    private val client: WebClient,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(TextExtractor::class.java)
    fun extractText(): Mono<String> {
        val pdfFile = File("src/main/resources/A012203309Y.pdf")

        // Encode and save the file
        val mimeType = "application/pdf"

        // Retrieve the content from the saved JSON object
        val jsonContent = utils.createRequestJson(pdfFile, mimeType)
        val jsonString = objectMapper.writeValueAsString(jsonContent)
        logger.info(jsonString)

        // Make the POST request
        return client.post()
            .uri("v1/projects/32694591112/locations/us/processors/5dcd993426673b7a/processorVersions/pretrained-ocr-v1.0-2020-09-23:process")
            .body(BodyInserters.fromValue(jsonContent))
            .retrieve()
            .bodyToMono(String::class.java)
            .doOnSuccess{extractedText ->
                logger.info("Successfully extracted text: $extractedText")
            }
            .doOnError{error ->
                logger.error("Failed to extract text", error)
            }
            .log()
            .subscribeOn(Schedulers.immediate())
        }
    }

