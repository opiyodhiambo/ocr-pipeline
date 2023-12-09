package com.adventure.ocrpipeline.actors

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import com.adventure.ocrpipeline.model.Command.*
import com.adventure.ocrpipeline.model.Message.*
import com.adventure.ocrpipeline.service.DetailsParser
import org.slf4j.LoggerFactory

class DocumentParserActor(
    private val parser: DetailsParser,
    private val context: ActorContext<Parse>): AbstractBehavior<Parse>(context) {
        private val log = LoggerFactory.getLogger(DocumentParserActor::class.java)
        companion object {
            fun create(parser: DetailsParser): Behavior<Parse> {
                return Behaviors.setup { context ->
                    DocumentParserActor(parser, context)
                }

            }
        }
    override fun createReceive(): Receive<Parse> {
        return newReceiveBuilder()
            .onMessage(Parse::class.java) { command ->
                log.info("Parsing data: $command")
                parser.parseIdFront(command.extractedText).subscribe { parsedData ->
                    command.replyTo.tell(ParsedText(parsedData))
                }
                Behaviors.same()
               }
                .build()
    }
}