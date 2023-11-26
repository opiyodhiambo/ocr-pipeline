package com.adventure.ocrpipeline.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

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
    fun s3client(): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey)))
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endPoint, region))
            .build()

    }
}