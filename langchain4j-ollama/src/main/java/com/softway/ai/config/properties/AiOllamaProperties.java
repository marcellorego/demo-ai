package com.softway.ai.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(value = "ai.ollama")
@Data
public class AiOllamaProperties {

  private String baseUrl;

  @NestedConfigurationProperty
  private OllamaChatProperties chat;
}
