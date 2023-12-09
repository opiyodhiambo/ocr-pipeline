package com.adventure.ocrpipeline.model

import akka.actor.typed.ActorRef
import com.adventure.ocrpipeline.model.DataModel.OCRRequested
import com.fasterxml.jackson.databind.JsonNode
import reactor.core.publisher.Mono

sealed class Command {
    data class Extract(
        val doc: OCRRequested,
        val replyTo: ActorRef<Message>
    ): Command()
    data class Parse(
        val extractedText: Mono<JsonNode>,
        val replyTo: ActorRef<Message>
    ): Command()
}