package com.adventure.ocrpipeline.utils

import com.adventure.ocrpipeline.service.JsonDataService
import com.google.auth.oauth2.GoogleCredentials
import org.springframework.stereotype.Component
import java.io.File
import java.util.*

@Component
class Utils(
    private val jsonDataService: JsonDataService
) {
    fun fetchAccessToken(): String {
        val credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS")
        if (credentialsJson.isNullOrBlank()){
            println("Error: GOOGLE_APPLICATION_CREDENTIALS environment variable is not set")
        }
        try{
            val credentials = GoogleCredentials.fromStream(credentialsJson.byteInputStream())
            val accessToken = credentials.accessToken
            if (accessToken != null){
                return accessToken.tokenValue
            } else{
                println("Errror: Access token is null")

            }

        } catch (e: Exception) {
            println("Error fetching access token: ${e.message}")

        }
        return ""
    }
    fun processAndLogResponse(response: String?) {
        if (response != null) {
            println("Response Content: $response")
        } else {
            println("Error: Response is null.")
        }
    }
    fun encodeAndSaveFile(file: File, mimetype: String) {
        val fileContent = file.readBytes()
        val base64Encoded = Base64.getEncoder().encode(fileContent)
        val jsonData = mapOf(
            "skipHumanReview" to true,
            "rawDocument" to mapOf(
                "mimetype" to mimetype,
                "content" to base64Encoded
            )
        )
        jsonDataService.saveData(jsonData)
    }

}
