package com.softway.ai.config;

import com.softway.ai.config.properties.AiOllamaProperties;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AiOllamaProperties.class)
public class AiConfig {

  private final AiOllamaProperties aiOllamaProperties;

  public AiConfig(AiOllamaProperties aiOllamaProperties) {
    this.aiOllamaProperties = aiOllamaProperties;
  }

  @ConditionalOnProperty(prefix = "ai.ollama.chat", name = "enabled", havingValue = "true")
  @Bean
  public ChatLanguageModel chatModel() {
    return OllamaChatModel.builder()
        .baseUrl(aiOllamaProperties.getBaseUrl())
        .modelName(aiOllamaProperties.getChat().getModel())
        .maxRetries(aiOllamaProperties.getChat().getOptions().maxRetries())
        .timeout(aiOllamaProperties.getChat().getOptions().timeout())
        .temperature(aiOllamaProperties.getChat().getOptions().temperature())
        .topP(aiOllamaProperties.getChat().getOptions().topP())
        .build();
  }
}
