package com.softway.ai.web.chat;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChatService {

    private final ChatLanguageModel chatModel;
    public final Assistant assistant;

    public ChatService(final ChatLanguageModel chatModel,
                       final Assistant assistant) {
        this.chatModel = chatModel;
        this.assistant = assistant;
    }

    private void logUsage(final TokenUsage tokenUsage) {
        log.debug("Usage is input: {}, output: {}, total: {}",
            tokenUsage.inputTokenCount(), tokenUsage.outputTokenCount(), tokenUsage.totalTokenCount());
    }

    public ChatResponse generateSystem(ChatRequest request) {
        final String userName = Optional.of(request)
            .map(ChatRequest::userId)
            .map(String::valueOf)
            .orElse("-");
        final UserMessage userMessage = UserMessage.from(userName, request.message());
        final SystemMessage systemMessage =
            SystemMessage.from("You are a story teller for children under 10 years old");
        final Response<AiMessage> aiMessageResponse =
            chatModel.generate(List.of(systemMessage, userMessage));
        logUsage(aiMessageResponse.tokenUsage());
        return new ChatResponse(aiMessageResponse.content().text());
    }

    public ChatResponse generateSimple(ChatRequest request) {
        final String userName = Optional.of(request)
            .map(ChatRequest::userId)
            .map(String::valueOf)
            .orElse("-");
        final UserMessage userMessage = UserMessage.from(userName, request.message());
        final Response<AiMessage> aiMessageResponse =
            chatModel.generate(userMessage);

        logUsage(aiMessageResponse.tokenUsage());
        return new ChatResponse(aiMessageResponse.content().text());
    }

    public ChatResponse generate(ChatRequest request) {
        return new ChatResponse(assistant.chat(request.userId(), request.message()));
    }

    public UserName extractUserName(ChatRequest request) {
        return assistant.extractUserName(request.message());
    }
}
