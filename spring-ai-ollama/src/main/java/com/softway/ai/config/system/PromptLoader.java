package com.softway.ai.config.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public final class PromptLoader {

    final ResourceLoader resourceLoader;

    public PromptLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private static final Map<String, PromptTemplate> SYSTEM_PROMPT_TEMPLATE = new ConcurrentHashMap<>();

    public PromptTemplate getPromptTemplate(final String key) {
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

}
