package com.softway.ai.web.chat;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody @Valid final ChatRequest chatRequest) {
        return chatService.generate(chatRequest);
    }

    @PostMapping("/extract")
    public UserName extractUserName(@RequestBody @Valid final ChatRequest chatRequest) {
        return chatService.extractUserName(chatRequest);
    }
}
