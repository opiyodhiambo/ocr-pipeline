package com.adventure.ocrpipeline.service

import com.adventure.ocrpipeline.utils.Utils
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
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

        // Retrieve the content from the saved JSON file
        val requestJsonContent = File("request.json").readText()

        // Make the POST request
        val responseMono: Mono<String> = client.post()
            .body(BodyInserters.fromValue(requestJsonContent))
            .retrieve()
            .bodyToMono(String::class.java)

        responseMono.subscribe{response ->

            if (response != null) {
                utils.encodeAndSaveFile(pdfFile, "application/pdf")
            }

            utils.processAndLogResponse(response)
        }
    }
}