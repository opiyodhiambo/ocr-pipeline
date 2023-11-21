package com.adventure.ocrpipeline.config

import com.adventure.ocrpipeline.Utils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class Config(utils: Utils) {
    final val endpointUrl =
        "https://us-documentai.googleapis.com/v1/projects/32694591112/locations/us/processors/5dcd993426673b7a/processorVersions/pretrained-ocr-v1.0-2020-09-23:process"

    // Get the access token using the gcloud command
    final val accessToken = utils.getAccessToken()

    @Bean
    // Set up WebClient
    fun webClient() = WebClient.builder()
        .baseUrl(endpointUrl)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()
}