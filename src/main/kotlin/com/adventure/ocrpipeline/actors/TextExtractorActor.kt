package com.adventure.ocrpipeline.actors

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import com.adventure.ocrpipeline.model.*
import com.adventure.ocrpipeline.model.Command.Extract
import com.adventure.ocrpipeline.model.Message.ExtractedText
import com.adventure.ocrpipeline.service.TextExtractor
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class TextExtractorActor(
    private val textExtractor: TextExtractor,
    private val context: ActorContext<Extract>): AbstractBehavior<Extract>(context) {
        private val log = LoggerFactory.getLogger(TextExtractorActor::class.java)
        companion object {
            fun create(textExtractor: TextExtractor): Behavior<Extract> {
                return Behaviors.setup { context ->
                    TextExtractorActor(textExtractor, context)

                    }
                }
            }
    override fun createReceive(): Receive<Extract> {
        return newReceiveBuilder()
            .onMessage(Extract::class.java) { command ->
                log.info(" Extracting text: $command")
                textExtractor.extractIdFront(command.doc).subscribe{ node ->
                    command.replyTo.tell(ExtractedText(Mono.just(node)))
                }
                Behaviors.same()
            }
            .build()
        }
    }

