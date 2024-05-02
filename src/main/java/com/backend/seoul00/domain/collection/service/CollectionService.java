package com.backend.seoul00.domain.collection.service;

import com.backend.seoul00.domain.collection.entity.CollectionJpaEntity;
import com.backend.seoul00.domain.collection.repository.CollectionRepository;
import com.backend.seoul00.domain.collection.repository.CustomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class CollectionService {

    private final CustomRepository customRepository;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final CollectionRepository collectionRepository;

    public CollectionService(CustomRepository customRepository, RestClient restClient, ObjectMapper objectMapper, CollectionRepository collectionRepository) {
        this.customRepository = customRepository;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.collectionRepository = collectionRepository;
    }

    // TODO : 사용자 위치 기반 데이터 API

    // TODO : 검색 데이터 API

    public void updateSeoulInfo() throws JsonProcessingException {
        for (CollectionJpaEntity entity : collectionRepository.findAll()) {
            if (entity.getId() < 19529) continue;

            String address = entity.getAddress();
            String mapResult = restClient.get()
                    .uri(uriBuilder -> getUri(uriBuilder, address))
                    .accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .onStatus(HttpStatusCode::is3xxRedirection,
                            ((request, response) -> {
                                throw new IllegalArgumentException("Invalid request");
                            }))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            ((request, response) -> {
                                throw new IllegalArgumentException("server error");
                            }))
                    .body(String.class);

            if (objectMapper.readTree(mapResult).get("documents").size() == 0) continue;

            try {
                JsonNode subInfo = objectMapper.readTree(mapResult)
                        .get("documents")
                        .get(0)
                        .get("address");

                String longitude;
                String latitude;
                if (subInfo == null) {
                    longitude = objectMapper.readTree(mapResult)
                            .get("documents")
                            .get(0)
                            .get("road_address")
                            .get("x")
                            .toString();

                    latitude = objectMapper.readTree(mapResult)
                            .get("documents")
                            .get(0)
                            .get("road_address")
                            .get("y")
                            .toString();
                } else {
                    longitude = subInfo.get("x").toString();
                    latitude = subInfo.get("y").toString();
                }

                entity.updatePoint(longitude, latitude);
                collectionRepository.save(entity);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private URI getUri(UriBuilder uriBuilder, String query) {
        UriBuilder builder = uriBuilder.queryParam("query", query);
        return builder.build();
    }
}
