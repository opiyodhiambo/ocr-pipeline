package com.adventure.ocrpipeline.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.NoSuchKeyException

@Service
class S3Service(private val digitalOceanClient: S3AsyncClient) {

    @Value("\${digital-ocean.bucket-name}")
    private val bucketName: String? = null
    companion object{
        var log = LoggerFactory.getLogger(this::class.java)
    }
    fun downloadDocument(folderName: String, fileName: String): Mono<ByteArray> {
        log.info("Downloading Document")
        val keyName = "$folderName/$fileName"
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(keyName)
            .build()
        log.info("getObjectRequested: $getObjectRequest")
        return Mono.create { sink ->
            log.info("Starting S3 download operation...")
            digitalOceanClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes())
                .whenComplete { response, error ->
                    if (error != null) {
                        if (error is NoSuchKeyException) {
                            log.error("NoSuchKeyException: Object not found. Key: $keyName", error)
                        } else {
                            log.error("Error during S3 download operation", error)
                        }
                        sink.error(error)
                    } else {
                        val content = response.asByteArray()
                        sink.success(content)
                    }
                }
            }
            .publishOn(Schedulers.boundedElastic())

        }
    }

