package com.adventure.ocrpipeline.config

import com.adventure.ocrpipeline.utils.Utils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class Config(utils: Utils) {
    @Value("\${google.cloud.api.endPointUrl}")
    private final val endpointUrl: String? = null
    // Get the access token using the gcloud command
    private final val accessToken = utils.fetchAccessToken()

    @Bean
    // Set up WebClient
    fun webClient() = WebClient.builder()
        .baseUrl(endpointUrl!!)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()
}