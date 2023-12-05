//package com.adventure.ocrpipeline
//
//import com.adventure.ocrpipeline.controller.OCRController
//import com.adventure.ocrpipeline.service.OCRService
//import com.adventure.ocrpipeline.service.TextExtractor
//import org.springframework.boot.CommandLineRunner
//import org.springframework.boot.autoconfigure.SpringBootApplication
//import org.springframework.boot.runApplication
//
//@SpringBootApplication
//class OcrPipelineApplication(
//	private val ocrService: OCRService,
//	private val contoller: OCRController,
//    private val textExtractor: TextExtractor
//) : CommandLineRunner {
//
//	override fun run(vararg args: String?) {
//		// Run the text extraction process
////		ocrService.classify().subscribe()
////		Thread.sleep(1000)
////		ocrService.getIdBack().subscribe()
//		textExtractor.extractIdFront().subscribe()
//	}
//
//}
//
//fun main(args: Array<String>) {
//	runApplication<OcrPipelineApplication>(*args)
//}
