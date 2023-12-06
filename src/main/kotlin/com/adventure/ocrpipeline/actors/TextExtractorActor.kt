package com.adventure.ocrpipeline.actors

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import com.adventure.ocrpipeline.model.DataModel.ExtractedText
import com.adventure.ocrpipeline.model.DataModel.OCRRequested
import com.adventure.ocrpipeline.service.TextExtractor

sealed class Extract
data class ExtractText(
    val event: OCRRequested,
    val replyTo: ActorRef<ExtractedText>
): Extract()

class TextExtractorActor(
    private val textExtractor: TextExtractor,
    private val context: ActorContext<Extract>): AbstractBehavior<Extract>(context) {
        companion object {
            fun create(textExtractor: TextExtractor): Behavior<Extract> {
                return Behaviors.setup { context ->
                    TextExtractorActor(textExtractor, context)
                }
            }
        }

    override fun createReceive(): Receive<Extract> {
        return newReceiveBuilder()
            .onMessage(ExtractText::class.java) { command ->
                val text = textExtractor.extractIdFront(command.event)
                command.replyTo.tell(ExtractedText(text))
            this
            }
            .build()
    }
}