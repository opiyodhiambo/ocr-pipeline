package com.adventure.ocrpipeline.service

import com.amazonaws.services.s3.AmazonS3
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import java.io.File

@Service
class S3Service(private val digitalOceanClient: AmazonS3) {

    @Value("\${digital-ocean.bucket-name}")
    private val bucketName: String? = null

    fun uploadDocument( file: File) {
        TODO()
    }


    fun downloadDocument(fileName: String) {


    }
}
//
//https://tajji-kyc-documentation-bucket.ams3.digitaloceanspaces.com
//https://tajji-kyc-documentation-bucket.ams3.digitaloceanspaces.com/KE-KRA-PIN-CERTIFICATE/A012203309Y.pdf