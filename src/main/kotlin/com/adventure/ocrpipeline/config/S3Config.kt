package com.adventure.ocrpipeline.config


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import java.net.URI

@Service
class S3Config {
    @Value("\${digital-ocean.access-key}")
    private val accessKey: String? = null

    @Value("\${digital-ocean.secret-key}")
    private val secretKey: String? = null

    @Value("\${digital-ocean.region}")
    private val region: String? = null

    @Value("\${digital-ocean.end-point}")
    private val endPoint: String? = null

    @Bean
    fun s3client(): S3AsyncClient {
        return S3AsyncClient.builder()
            .region(region?.let {Region.of(it)})
            .endpointOverride(endPoint?.let {URI.create(it)})
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build()
    }
}

