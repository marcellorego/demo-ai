package com.softway.ai.web;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/template")
@Slf4j
public class PromptTemplateController {

  private static final Map<String, PromptTemplate> SYSTEM_PROMPT_TEMPLATE = new ConcurrentHashMap<>();

  private final ResourceLoader resourceLoader;
  private final ChatClient chatClient;

  public PromptTemplateController(final ResourceLoader resourceLoader,
                                  final ChatClient.Builder chatClientBuilder) {
    this.resourceLoader = resourceLoader;
    this.chatClient = chatClientBuilder.build();
  }

  private PromptTemplate getPromptTemplate(final String key) {
    return SYSTEM_PROMPT_TEMPLATE.computeIfAbsent(key, k -> {
      final Resource resource = resourceLoader.getResource("classpath:/templates/" + k + ".st");
      if (resource.exists()) {
        if (k.contains("system")) {
          return new SystemPromptTemplate(resource);
        } else {
          return new PromptTemplate(resource);
        }
      }
      return null;
    });
  }

  private Generation generateResponse(final Prompt prompt) {

    final Generation generation = chatClient
        .prompt(prompt)
        .call()
        .chatResponse()
        .getResult();

    log.info("Generated prompt contents: {}", prompt.getContents());
    log.info("Generated response: {}", generation);
    log.info("Generated response output content: {}", generation.getOutput().getContent());

    return generation;
  }

  @GetMapping("/ai")
  String generation(@RequestParam("comment") final String comment) {

    final Prompt prompt = getPromptTemplate("user/sentiment").create(Map.of("comment", comment));

    final Generation generation = generateResponse(prompt);

    return String.format("You should be: `%s`", generation.getOutput().getContent());
  }

  @GetMapping("/tweet")
  String tweet(@RequestParam(value = "content") final String content) {

    final Prompt prompt = getPromptTemplate("user/tweet").create(Map.of("content", content));

    final Generation generation = generateResponse(prompt);

    return String.format(generation.getOutput().getContent());
  }

  @GetMapping("/tweet_convo")
  String tweetConvo(@RequestParam(value = "content", required = false) final String content) {

    final PromptTemplate systemMessage = getPromptTemplate("system/tweet");

    final Message systemPromptMessage = systemMessage.createMessage(Map.of("name", "Tweet Sentiment Analyst"));

    final Message userMessage = new UserMessage(content);

    final Prompt prompt = new Prompt(List.of(systemPromptMessage, userMessage));

    final Generation generation = generateResponse(prompt);

    return String.format(generation.getOutput().getContent());
  }
}