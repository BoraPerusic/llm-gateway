package com.llmgateway.async

import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.nats.NatsTemplate

@Configuration
class NatsConfig {

    @Bean
    fun natsConnection(): Connection {
        // In real app, load from config
        return Nats.connect("nats://localhost:4222")
    }

    @Bean
    fun natsTemplate(connection: Connection): NatsTemplate {
        return NatsTemplate(connection)
    }
}

@Configuration
class NatsPublisher(private val natsTemplate: NatsTemplate) {

    fun publishJobCompleted(job: Job) {
        val subject = "jobs.completed"
        // Simple serialization
        val payload = """{"jobId": "${job.id}", "status": "${job.status}"}"""
        natsTemplate.send(
                subject,
                org.springframework.messaging.support.MessageBuilder.withPayload(payload).build()
        )
    }
}
