package com.adventure.ocrpipeline.utils


import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.*

@Component
class Utils() {
    private val logger = LoggerFactory.getLogger(Utils::class.java)
    fun processAndLogResponse(response: Any?) {
        if (response != null) {
            logger.info("Response Content: $response")
        } else {
            logger.info("Error: Response is null.")
        }
    }
    fun createRequestJson(file: Mono<ByteArray>, mimetype: String): Mono<Map<String, Any>> {
        return file.map { byteArray ->
            val base64Encoded = Base64.getEncoder().encodeToString(byteArray)
            mapOf(
                "skipHumanReview" to true,
                "rawDocument" to mapOf(
                    "mimeType" to mimetype,
                    "content" to base64Encoded
                )
            )
        }
    }
    fun classifyDocument(jsonNode: Mono<JsonNode>): Mono<String>{
        return jsonNode.flatMap { node ->
            Mono.fromCallable {
                val entities = node.elements()
                var maxConfidence = 0.0
                var highestConfidenceType = ""
                while (entities.hasNext()) {
                    val entity = entities.next()
                    val confidence = entity["confidence"].asDouble()
                    if (confidence > maxConfidence) {
                        maxConfidence = confidence
                        highestConfidenceType = entity["type"].asText()
                    }
                }
                logger.info("the document is $highestConfidenceType")
                highestConfidenceType
            }
                .subscribeOn(Schedulers.immediate())
        }

    }

}
