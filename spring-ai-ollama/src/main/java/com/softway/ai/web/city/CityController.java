package com.softway.ai.web.city;

import com.softway.ai.config.system.PromptLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final ChatClient chatClient;

    public CityController(final PromptLoader promptLoader,
                          final ChatClient.Builder builder) {
        final String system = promptLoader.getPromptTemplate("system/city").getTemplate();
        this.chatClient = builder
            .defaultSystem(system)
            .defaultFunctions("currentWeatherFunction")
            .build();
    }

    @GetMapping
    public String cityFaq(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .call()
            .content();
    }
}