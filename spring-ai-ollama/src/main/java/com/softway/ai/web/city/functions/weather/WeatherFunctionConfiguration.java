package com.softway.ai.web.city.functions.weather;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
@EnableConfigurationProperties(WeatherConfigProperties.class)
public class WeatherFunctionConfiguration {

    private final WeatherConfigProperties props;

    public WeatherFunctionConfiguration(WeatherConfigProperties props) {
        this.props = props;
    }

    @Bean
    @Description("Get the current weather conditions for the given city.")
    public Function<WeatherService.Request, WeatherService.Response> currentWeatherFunction() {
        return new WeatherService(props);
    }

}