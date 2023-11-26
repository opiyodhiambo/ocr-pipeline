package com.adventure.ocrpipeline.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.PutObjectResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import java.io.File

@Service
class S3Service(private val digitalOceanClient: AmazonS3) {

    @Value("\${digital-ocean.bucket-name}")
    private val bucketName: String? = null

    fun uploadDocument(file: File, key: String, mimeType: String): PutObjectResult {
        val content = file.readBytes()
//        val metadata = ObjectMetadata().apply {
//            contentType = mimeType
//            contentLength = content.size.toLong()
//        }
        return digitalOceanClient.putObject(
            bucketName,
            key,
            file
        )
    }


    fun downloadDocument(fileName: String) {


    }
}
//
//https://tajji-kyc-documentation-bucket.ams3.digitaloceanspaces.com
//https://tajji-kyc-documentation-bucket.ams3.digitaloceanspaces.com/KE-KRA-PIN-CERTIFICATE/A012203309Y.pdf