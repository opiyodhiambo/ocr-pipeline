package com.adventure.ocrpipeline.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class DocAiConfig() {
    @Value("\${google.cloud.api.base-url}")
    private final val baseUrl: String? = null
    // Get the access token using the gcloud command
    @Value("\${google.cloud.access-token}")
    private final val accessToken: String? = null
    @Bean
    // Set up WebClient
    fun webClient(): WebClient {
        val size = 16*1024*1024
        val strategies = ExchangeStrategies.builder()
            .codecs { codecs ->
                codecs.defaultCodecs().maxInMemorySize(size)
            }
            .build()
        return WebClient.builder()
            .baseUrl(baseUrl!!)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchangeStrategies(strategies)
            .build()

    }
}