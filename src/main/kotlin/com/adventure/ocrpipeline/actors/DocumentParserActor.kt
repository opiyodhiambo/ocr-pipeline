package com.adventure.ocrpipeline.actors

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import com.adventure.ocrpipeline.model.DataModel
import com.adventure.ocrpipeline.model.DataModel.*
import com.adventure.ocrpipeline.service.DetailsParser
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.GenericEventMessage


sealed class Parse
data class ParseText(
    val message: ExtractedText
): Parse()
class DocumentParserActor(
    private val parser: DetailsParser,
    private val context: ActorContext<Parse>,
    private val eventBus: EventBus): AbstractBehavior<Parse>(context) {
        companion object {
            fun create(parser: DetailsParser, eventBus: EventBus): Behavior<Parse> {
                return Behaviors.setup { context ->
                    DocumentParserActor(parser, context, eventBus)
                }

            }
        }
    override fun createReceive(): Receive<Parse> {
        return newReceiveBuilder()
            .onMessage(ParseText::class.java) { command ->
                parser.parseIdFront(command.message.text)
                    .doOnSuccess { text ->
                        eventBus.publish(GenericEventMessage.asEventMessage<DocumentExtracted>(text))
                    }
                    .subscribe()
                this
                }
                .build()

    }
}