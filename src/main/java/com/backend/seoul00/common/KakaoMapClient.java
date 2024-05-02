package com.backend.seoul00.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class KakaoMapClient {

    @Value("${client.auth.kakao.map.key}")
    private String key;

    @Value("${client.auth.kakao.map.uri}")
    private String uri;

    private static final String AUTH_HEADER_PREFIX = "KakaoAK ";

    @Bean
    public RestClient buildClient() {
        return RestClient.builder()
                .baseUrl(uri)
                .defaultHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_PREFIX + key)
                .build();
    }
}
