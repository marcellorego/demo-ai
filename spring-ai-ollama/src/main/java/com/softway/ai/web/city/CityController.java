package com.softway.ai.web.city;

import com.softway.ai.config.system.PromptLoader;
import org.springframework.ai.ResourceUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
    public Flux<String> cityFaq(@RequestParam String message) {
        return chatClient.prompt()
            .user(message)
            .stream()
            .content();
    }
}