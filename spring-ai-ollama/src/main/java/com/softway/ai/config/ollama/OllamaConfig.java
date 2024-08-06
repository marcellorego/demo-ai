package com.softway.ai.config.ollama;

import org.springframework.ai.autoconfigure.ollama.OllamaChatProperties;
import org.springframework.ai.autoconfigure.ollama.OllamaConnectionProperties;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

public class OllamaConfig {

  private final OllamaConnectionProperties ollamaConnectionProperties;
  private final OllamaChatProperties ollamaChatProperties;

  public OllamaConfig(final OllamaConnectionProperties ollamaConnectionProperties,
                      final OllamaChatProperties ollamaChatProperties) {
    this.ollamaConnectionProperties = ollamaConnectionProperties;
    this.ollamaChatProperties = ollamaChatProperties;
  }

  public OllamaConnectionProperties getOllamaConnectionProperties() {
    return ollamaConnectionProperties;
  }

  public OllamaChatProperties getChatProperties() {
    return ollamaChatProperties;
  }

  public ChatModel provideChatModel() {
    final OllamaApi ollamaApi = new OllamaApi(this.getOllamaConnectionProperties().getBaseUrl());
    return new OllamaChatModel(
        ollamaApi,
        OllamaOptions.create()
            .withModel(this.getChatProperties().getOptions().getModel())
            .withTemperature(this.getChatProperties().getOptions().getTemperature())
    );
  }
}