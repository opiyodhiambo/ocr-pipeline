package com.adventure.ocrpipeline

import com.adventure.ocrpipeline.service.TextExtractor
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OcrPipelineApplication(
	private val textExtractor: TextExtractor
) : CommandLineRunner {

	override fun run(vararg args: String?) {
		// Run the text extraction process
		textExtractor.extractText().subscribe()
		Thread.sleep(1000)
	}
}

fun main(args: Array<String>) {
	runApplication<OcrPipelineApplication>(*args)
}
