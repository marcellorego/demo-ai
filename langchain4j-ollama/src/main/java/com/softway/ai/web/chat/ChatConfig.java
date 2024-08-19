package com.softway.ai.web.chat;

import com.softway.ai.config.properties.AiOllamaProperties;
import com.softway.ai.config.properties.OllamaChatProperties;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    private final OllamaChatProperties chatProperties;

    public ChatConfig(AiOllamaProperties aiOllamaProperties) {
        this.chatProperties = aiOllamaProperties.getChat();
    }

    @Bean
    public Assistant assistant(final ChatLanguageModel chatModel) {
        return AiServices.builder(Assistant.class)
            .chatLanguageModel(chatModel)
            .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(chatProperties.getMemory().maxMessages()))
            .build();
    }
}
