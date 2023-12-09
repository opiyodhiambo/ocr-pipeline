package com.adventure.ocrpipeline.actors

import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.Behaviors
import com.adventure.ocrpipeline.model.*
import com.adventure.ocrpipeline.model.Command.Extract
import com.adventure.ocrpipeline.model.Command.Parse
import com.adventure.ocrpipeline.model.DataModel.NationalIdData
import com.adventure.ocrpipeline.model.DataModel.OCRRequested
import com.adventure.ocrpipeline.model.Message.*
import com.adventure.ocrpipeline.service.DetailsParser
import com.adventure.ocrpipeline.service.TextExtractor
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.GenericEventMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ActorSystemInitializer(
    private val eventBus: EventBus,
    private val textExtractor: TextExtractor,
    private val detailsParser: DetailsParser
) {
    private val log = LoggerFactory.getLogger(ActorSystemInitializer::class.java)
    private val actorSystem: ActorSystem<Message> = ActorSystem.create(
        Behaviors.setup<Message> { context ->
            val textExtractorActor =
                context.spawn(TextExtractorActor.create(textExtractor),
                    "TextExtractionActor")
            val documentParserActor =
                context.spawn(DocumentParserActor.create(detailsParser),
                    "DocumentParserActor")

            Behaviors.receiveMessage<Message> { message ->
                when (message) {
                    is Event -> {
                        val extractText = Extract(message.event, context.self)
                        textExtractorActor.tell(extractText)
                    }
                    is ExtractedText -> {
                        val parseText = Parse(message.text, context.self)
                        documentParserActor.tell(parseText)
                    }
                    is ParsedText -> {
                        log.info("${message.nationalIdData}")
                        eventBus.publish(GenericEventMessage
                            .asEventMessage<ParsedText>(message.nationalIdData))
                    }
                }
                Behaviors.same()
            }

        }, "OCRSystem"
    )

    fun process(event: OCRRequested){
        actorSystem.tell(Event(event))
    }

}
