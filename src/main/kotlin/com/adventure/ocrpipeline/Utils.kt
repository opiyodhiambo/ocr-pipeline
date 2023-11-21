package com.adventure.ocrpipeline

import org.springframework.stereotype.Component
import java.io.File
import java.util.*

@Component
class Utils(
    private val jsonDataService: JsonDataService
) {
    fun getAccessToken(): String {
        // Use the gcloud command to obtain the access token
        return "ya29.a0AfB_byA8p5zvF65MWK17pHLbYOhey9RGQXbEL4_pjie7-azvWGO9q3-GfAqFpAEkiEhUl77-Gl4sIvo69yiWqrRD73nkqHLwJoRIVkuPOSesph23kPNA73XF266lepGpnuRuyF5AjntryXwvl1TxXRPKQORCwnbiGlO8E31vXhoaCgYKAVESARASFQHGX2Mi-KAcm3JO4TyWbzf-5juuKg0178"
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