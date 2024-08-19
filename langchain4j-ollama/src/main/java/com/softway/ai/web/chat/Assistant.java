package com.softway.ai.web.chat;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface Assistant {

    @SystemMessage(fromResource = "/templates/system/child.st")
    String chat(@MemoryId final Integer memId, @UserMessage final String question);

    @SystemMessage(fromResource = "/templates/system/extractor.st")
    @UserMessage("Make it in json format")
    UserName extractUserName(@V("text") final String text);
}
