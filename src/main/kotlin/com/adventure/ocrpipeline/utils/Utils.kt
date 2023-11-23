package com.adventure.ocrpipeline.utils

import com.adventure.ocrpipeline.service.JsonDataService
import com.adventure.ocrpipeline.service.TextExtractor
import com.google.auth.oauth2.GoogleCredentials
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
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
    fun createRequestJson(file: File, mimetype: String): Map<String, Any> {
        val fileContent = file.readBytes()
        val base64Encoded = Base64.getEncoder().encodeToString(fileContent)
        val jsonData =  mapOf(
            "skipHumanReview" to true,
            "rawDocument" to mapOf(
                "mimeType" to mimetype,
                "content" to base64Encoded
            )
        )
        jsonDataService.saveData(jsonData)
        return jsonData
    }

}
