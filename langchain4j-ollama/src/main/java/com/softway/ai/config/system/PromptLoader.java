package com.softway.ai.config.system;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public final class PromptLoader {

    final ResourceLoader resourceLoader;

    public PromptLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

//    private static final Map<String, PromptTemplate> SYSTEM_PROMPT_TEMPLATE = new ConcurrentHashMap<>();

//    public PromptTemplate getPromptTemplate(final String key) {
//        return SYSTEM_PROMPT_TEMPLATE.computeIfAbsent(key, k -> {
//            final Resource resource = resourceLoader.getResource("classpath:/templates/" + k + ".st");
//            if (resource.exists()) {
//                if (k.contains("system")) {
//                    return new SystemPromptTemplate(resource);
//                } else {
//                    return new PromptTemplate(resource);
//                }
//            }
//            return null;
//        });
//    }

}
