package com.backend.oneqjob.companyUser.service;

import com.backend.oneqjob.companyUser.dto.BusinessNumberRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;


@Service
@Slf4j
@Getter
@Setter
public class BusinessNumberCheckService {

    private final WebClient webClient;
    private final String apiUrl;
    private final String apiKey;


    public BusinessNumberCheckService(WebClient webClient,
                                      @Value("${odcloud.api.url}") String apiUrl,
                                      @Value("${odcloud.api.key}") String apiKey) {
        this.webClient = webClient;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }


    public Mono<BusinessNumberRequest> check(BusinessNumberRequest number) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(apiUrl)
                        .queryParam("apiKey", apiKey)
                        .build())
                .bodyValue(number)
                .retrieve()
                .bodyToMono(BusinessNumberRequest.class)
                .doOnSuccess(result -> log.info("*****사업자번호 체크 ******"))
                .doOnError(error -> log.error("Error: {}", error.getMessage()));
    }
}


