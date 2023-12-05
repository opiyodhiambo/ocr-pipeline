package com.adventure.ocrpipeline.controller


import com.adventure.ocrpipeline.model.DataModel
import com.adventure.ocrpipeline.model.DataModel.*
import com.adventure.ocrpipeline.service.TextExtractor
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.GenericEventMessage
import org.axonframework.extensions.reactor.eventhandling.gateway.ReactorEventGateway
import org.axonframework.messaging.MetaData
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@ComponentScan(basePackages = ["com.adventure.ocrpipeline"])
class YourApplication

fun main(args: Array<String>) {
    runApplication<YourApplication>(*args)
}
@RestController
class OCRController(@Autowired private val textExtractor: TextExtractor, private val eventBus: EventBus) {
    companion object{
        var log = LoggerFactory.getLogger(OCRController::class.java)
    }
    @PostMapping("/ocrRequest")
    fun requestOCR(@RequestBody documentData: DocumentData): ResponseEntity<String> {
        val ocrRequestedEvent = OCRRequested(documentData)
        eventBus.publish(GenericEventMessage.asEventMessage<OCRRequested>(ocrRequestedEvent))
        log.info("Published OCRRequested event, $ocrRequestedEvent")
        return ResponseEntity.ok("OCRRequested Event successfully emmitted")
    }

    @EventHandler
    fun on(ocrRequested: OCRRequested) {
        log.info("Received OCRRequested event, $ocrRequested")
        textExtractor.extractIdFront(ocrRequested).subscribe()
    }


}