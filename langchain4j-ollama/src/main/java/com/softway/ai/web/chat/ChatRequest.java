package com.softway.ai.web.chat;

import jakarta.validation.constraints.NotNull;

public record ChatRequest(@NotNull(message = "user id field should be provided") Integer userId,
                          @NotNull(message = "message field should be provided") String message) {
}
