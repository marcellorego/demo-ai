package com.softway.ai.web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {

  private final ChatClient chatClient;

  //AiClient aiClient;

  public ChatController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @GetMapping("/ai")
  ChatResponse generation(@RequestParam("prompt") final String prompt) {

    final String message = this.chatClient.prompt()
        .user(prompt)
        .call()
        .content();

    return ChatResponse.builder()
        .message(message)
        .build();
  }

  @GetMapping("/stream")
  public Flux<ChatResponse> getStream(@RequestParam("prompt") final String prompt) {
    return this.chatClient.prompt()
        .user(prompt)
        .stream()
        .content()
        .map(c -> ChatResponse.builder().message(c).build());
  }

  @GetMapping("/streams")
  public Flux<String> getStreamString(@RequestParam("prompt") final String prompt) {
    return this.chatClient.prompt()
        .user(prompt)
        .stream()
        .content();
  }
}
