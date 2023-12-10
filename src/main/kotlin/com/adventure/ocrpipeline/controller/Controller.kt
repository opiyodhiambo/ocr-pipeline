package com.adventure.ocrpipeline.controller


import com.adventure.ocrpipeline.actors.ActorSystemInitializer
import com.adventure.ocrpipeline.model.DataModel.*
import com.adventure.ocrpipeline.service.OCRService
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.GenericEventMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@SpringBootApplication
@ComponentScan(basePackages = ["com.adventure.ocrpipeline"])
class YourApplication

fun main(args: Array<String>) {
    runApplication<YourApplication>(*args)
}
@RestController
class OCRController(
    @Autowired
    private val ocrService: OCRService,
    private val eventBus: EventBus,
    private val actorSystem: ActorSystemInitializer

) {
    companion object{
        var log = LoggerFactory.getLogger(OCRController::class.java)

    }
    @PostMapping("/ocrRequest")
    fun requestOCR(@RequestBody documentData: DocumentData): Mono<ResponseEntity<String>> {
        val ocrRequestedEvent = OCRRequested(documentData)
        eventBus.publish(GenericEventMessage
            .asEventMessage<OCRRequested>(ocrRequestedEvent))
        log.info("Published OCRRequested event, $ocrRequestedEvent")
        return ocrService.validateDocument(ocrRequestedEvent)
            .map { extractedData ->
                log.info("Classified document, $extractedData")
                ResponseEntity.ok("Classified document")
            }
    }

    @EventHandler
    fun on(ocrRequested: OCRRequested) {
        log.info("Received OCRRequested event, $ocrRequested")
        actorSystem.process(ocrRequested)
        log.info("Started ActorSystem $actorSystem")
    }

}