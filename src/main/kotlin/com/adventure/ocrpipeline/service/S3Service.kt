package com.adventure.ocrpipeline.service

import com.amazonaws.services.s3.AmazonS3
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class S3Service(private val digitalOceanClient: AmazonS3) {

    @Value("\${digital-ocean.bucket-name}")
    private val bucketName: String? = null

    fun uploadDocument() {
        TODO()
    }

    fun downloadDocument() {
        TODO()
    }
}