package com.wiremock.demo.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class WireMockConfig {
    private int port = 9999;
    private boolean recording = false;
    private String url = "http://universities.hipolabs.com";
}
