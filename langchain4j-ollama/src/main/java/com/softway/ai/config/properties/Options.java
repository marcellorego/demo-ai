package com.softway.ai.config.properties;

import java.time.Duration;

public record Options(Integer maxRetries, Duration timeout, Double temperature, Double topP) {
}