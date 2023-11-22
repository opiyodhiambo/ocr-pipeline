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
    fun fetchAccessToken(): String {
        val credentialsJson = System.getenv("GCLOUD_ACCESS_TOKEN")
        if (credentialsJson.isNullOrBlank()){
            println("Error: GOOGLE_APPLICATION_CREDENTIALS environment variable is not set")
        } else{
            println("Variable value: $credentialsJson")
        }
        try{
            val credentials = GoogleCredentials.fromStream(credentialsJson.byteInputStream())
            val accessToken = credentials.accessToken
            if (accessToken != null){
                println("Successfully retrieved access token: $accessToken")
                return accessToken.tokenValue
            } else{
                println("Error: Access token is null")

            }

        } catch (e: Exception) {
            println("Error fetching access token: ${e.message}")

        }
        return ""
//        try {
//            val credentials = GoogleCredentials
//                .fromStream( FileInputStream("/home/opiyo/Downloads/tajji-kyc-12a2b9d14772.json"))
//                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"))
//            credentials.refreshAccessToken()
//            println(credentials.accessToken)
//            val accessToken: AccessToken = credentials.accessToken
//            println(accessToken.tokenValue)
//            return accessToken.tokenValue
//        } catch (e: Exception) {
//            println("Error fetching access token: ${e.message}")
//        }
//         return ""

    }
    fun processAndLogResponse(response: String?) {
        if (response != null) {
            println("Response Content: $response")
        } else {
            println("Error: Response is null.")
        }
    }
    fun createRequestJson(file: File, mimetype: String): Map<String, Any> {
        val fileContent = file.readBytes()
        val base64Encoded = Base64.getEncoder().encodeToString(fileContent)
        val logger = LoggerFactory.getLogger(Utils::class.java)
        logger.info(base64Encoded)
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
