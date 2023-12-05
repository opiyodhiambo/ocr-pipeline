package com.adventure.ocrpipeline.utils

import com.adventure.ocrpipeline.service.JsonDataService
import com.adventure.ocrpipeline.service.TextExtractor
import com.google.auth.oauth2.GoogleCredentials
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.io.File
import java.io.FileInputStream
import java.util.*

@Component
class Utils(
    private val jsonDataService: JsonDataService
) {

    fun processAndLogResponse(response: Any?) {
        val logger = LoggerFactory.getLogger(Utils::class.java)
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

}
