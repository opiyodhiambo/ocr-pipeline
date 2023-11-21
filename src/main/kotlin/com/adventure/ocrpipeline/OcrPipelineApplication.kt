package com.adventure.ocrpipeline

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.util.*

@SpringBootApplication
class OcrPipelineApplication : CommandLineRunner {

	override fun run(vararg args: String?) {
		// Retrieve the directory as a File
		val directoryPath = ClassPathResource("data").file.path

		val files = File(directoryPath).listFiles()

		// Check if there are any files in the directory
		if (files != null && files.isNotEmpty()) {
			// Process each file
			for (pdfFile in files) {
				// Read the content of the PDF file
				val pdfContent = pdfFile.readBytes()

				// Encode the content to base64
				val base64Encoded = Base64.getEncoder().encodeToString(pdfContent)

				// Create the JSON object for each file
				val jsonData = mapOf(
					"skipHumanReview" to true,
					"rawDocument" to mapOf(
						"mimeType" to "application/pdf",
						"content" to base64Encoded
					)
				)

				// Print or use the JSON data as needed
				println(jsonData)
			}
		} else {
			println("No files found in the directory.")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<OcrPipelineApplication>(*args)
}
