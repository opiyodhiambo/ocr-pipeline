package com.adventure.ocrpipeline

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.io.File

@Service
class TextExtractor(
    private val jSonDataService: JsonDataService,
    private val utils: Utils,
    private val client: WebClient
) {

    fun extractText() {
        val pdfFile = File("src/main/resources/data/A012203309Y.pdf")

        // Encode and save the file
        val mimeType = "application/pdf"
        utils.encodeAndSaveFile(pdfFile, mimeType)
        

        // Make the POST request
        val response = client.post()
            .body(BodyInserters.fromValue(jsonData))
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        if (response != null) {
            utils.encodeAndSaveFile(pdfFile, "application/pdf")
        }

        utils.processAndLogResponse(response)
    }



}