package com.adventure.ocrpipeline.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.io.File

@Service
class JsonDataService(private val objectMapper: ObjectMapper) {
    fun saveData(jsonObject: Map<String, Any>) {
        val jsonDataBytes = objectMapper.writeValueAsBytes(jsonObject)
        File("request8.json").writeBytes(jsonDataBytes)
    }
}