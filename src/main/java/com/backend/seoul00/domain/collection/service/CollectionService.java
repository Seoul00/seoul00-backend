package com.backend.seoul00.domain.collection.service;

import com.backend.seoul00.domain.collection.dto.SearchResponseDto;
import com.backend.seoul00.domain.collection.dto.SearchRequestDto;
import com.backend.seoul00.domain.collection.dto.SearchTypeRequestDto;
import com.backend.seoul00.domain.collection.dto.SliceResponse;
import com.backend.seoul00.domain.collection.entity.CollectionJpaEntity;
import com.backend.seoul00.domain.collection.entity.Type;
import com.backend.seoul00.domain.collection.repository.CollectionRepository;
import com.backend.seoul00.domain.collection.repository.CustomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

    @Transactional
    public SliceResponse<CollectionJpaEntity> findByType(
            SearchRequestDto request,
            SearchTypeRequestDto searchType) {
        if (searchType.getTypes().isEmpty()) {
            List<Type> list = new ArrayList<>(
                    Arrays.asList(Type.values())
            );
            searchType.setTypes(list);
        }

        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Slice<CollectionJpaEntity> result = customRepository.searchPosByQuery(request.getQuery(), searchType.getTypes(), pageable);
        return new SliceResponse<>(result);
    }

    public void updateSeoulInfo() throws JsonProcessingException {
        for (CollectionJpaEntity entity : collectionRepository.findAll()) {
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

    public List<SearchResponseDto> getByDist(double latitude, double longitude, double radius) {
        List<CollectionJpaEntity> entities;
        try {
            entities = collectionRepository.findAll();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }

        return entities.stream()
                .filter(entity -> calculateDistance(latitude, longitude, entity.getLatitude(), entity.getLongitude()) < radius)
                .map(entity -> new SearchResponseDto(
                        entity.getLatitude(), entity.getLongitude(), entity.getAddress(), entity.getType()))
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371;
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }

    public List<SearchResponseDto> getByAddress(String address) {
        List<CollectionJpaEntity> entities;
        try {
            entities = collectionRepository.findByAddressContaining(address);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }

        return entities.stream()
                .map(entity -> new SearchResponseDto(
                        entity.getLatitude(), entity.getLongitude(), entity.getAddress(), entity.getType()))
                .collect(Collectors.toList());
    }

}
