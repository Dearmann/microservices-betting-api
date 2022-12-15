package com.github.dearmann.matchservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Profile("k8s")
public class WebClientConfigK8s {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
