package com.llmgateway.web

import com.llmgateway.model.ModelService
import com.llmgateway.rules.RequestMetadata
import com.llmgateway.rules.RuleEngine
import org.springframework.ai.chat.ChatClient
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Simple DTOs to mirror OpenAI structure roughly
data class ChatCompletionRequest(val model: String? = null, val messages: List<ChatMessageDto>)

data class ChatMessageDto(val role: String, val content: String)

data class ChatCompletionResponse(val content: String)

@RestController
@RequestMapping("/api/v1/chat")
class ChatController(
        private val ruleEngine: RuleEngine,
        private val modelService: ModelService,
        private val defaultChatClient: ChatClient // Fallback
) {

    @PostMapping("/completions")
    fun chat(@RequestBody request: ChatCompletionRequest): ChatCompletionResponse {
        val allModels = modelService.findAll()
        val metadata = RequestMetadata(modelName = request.model)
        val selectedModel = ruleEngine.selectModel(metadata, allModels)

        // TODO: In a real implementation, we would use a ClientFactory to get a ChatClient
        // configured with selectedModel.config (baseUrl, apiKey).
        // For now, we log the selection and use the default.
        println("Selected Model: ${selectedModel?.name} (Provider: ${selectedModel?.provider})")

        val messages = request.messages.map { toAiMessage(it) }
        val prompt = Prompt(messages)
        val response = defaultChatClient.call(prompt)
        return ChatCompletionResponse(response.result.output.content)
    }

    private fun toAiMessage(dto: ChatMessageDto): Message {
        return when (dto.role.lowercase()) {
            "user" -> UserMessage(dto.content)
            "system" -> SystemMessage(dto.content)
            "assistant" -> AssistantMessage(dto.content)
            else -> UserMessage(dto.content) // Fallback
        }
    }
}
