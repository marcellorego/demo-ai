package com.softway.ai.web;

import com.softway.ai.config.system.PromptLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/template")
@Slf4j
public class PromptTemplateController {

  private final PromptLoader promptLoader;
  private final ChatClient chatClient;

  public PromptTemplateController( final PromptLoader promptLoader,
                                   final ChatClient.Builder chatClientBuilder) {
    this.promptLoader = promptLoader;
    this.chatClient = chatClientBuilder.build();
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

    final Prompt prompt = promptLoader.getPromptTemplate("user/sentiment").create(Map.of("comment", comment));

    final Generation generation = generateResponse(prompt);

    return String.format("You should be: `%s`", generation.getOutput().getContent());
  }

  @GetMapping("/tweet")
  String tweet(@RequestParam(value = "content") final String content) {

    final Prompt prompt = promptLoader.getPromptTemplate("user/tweet").create(Map.of("content", content));

    final Generation generation = generateResponse(prompt);

    return String.format(generation.getOutput().getContent());
  }

  @GetMapping("/tweet_convo")
  String tweetConvo(@RequestParam(value = "content", required = false) final String content) {

    final PromptTemplate systemMessage = promptLoader.getPromptTemplate("system/tweet");

    final Message systemPromptMessage = systemMessage.createMessage(Map.of("name", "Tweet Sentiment Analyst"));

    final Message userMessage = new UserMessage(content);

    final Prompt prompt = new Prompt(List.of(systemPromptMessage, userMessage));

    final Generation generation = generateResponse(prompt);

    return String.format(generation.getOutput().getContent());
  }
}