package com.adventure.ocrpipeline

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class JsonDataService(private val objectMapper: ObjectMapper) {
    fun saveData(jsonObject: Map<String, Any>) {
        val jsonDataBytes = objectMapper.writeValueAsBytes(jsonObject)
        File("request.json").writeBytes(jsonDataBytes)
    }
}