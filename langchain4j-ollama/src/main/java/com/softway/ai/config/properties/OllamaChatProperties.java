package com.softway.ai.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


@Data
public class OllamaChatProperties {

  private boolean enabled;
  private String model;

  @NestedConfigurationProperty
  private MemoryOptions memory;

  @NestedConfigurationProperty
  private Options options;
}