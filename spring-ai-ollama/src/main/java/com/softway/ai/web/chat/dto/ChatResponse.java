package com.softway.ai.web.chat.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class ChatResponse {

  private String message;
}