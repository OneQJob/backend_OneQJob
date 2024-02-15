package com.backend.oneqjob.companyUser.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class BusinessNumberVerificationService {

    private final WebClient webClient;


    public String businessRegistration(String companyAccount, HttpSession session) {

        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("https://api.odcloud.kr/api/nts-businessman/v1/status")
//                        .queryParam("serviceKey", serviceKey)
                        .queryParam("r3wBopIepuHn1HY2zHPP0XjJM6y9ghRnAEub8GdiDo4QrLKAJHrAmcaFmVUyXmo/Mtl/2iwBbCksBAJ/o97DjA==")
                        .build())
                .bodyValue(companyAccount)
                .retrieve()
                .bodyToMono(String.class) // 예제에서는 응답 타입을 String으로 가정합니다.
                .subscribe(
                        result -> {
                            session.setAttribute("businessRegistrationVerified", true);
                        },
                        error -> {
                            // 에러 처리 로직
                            System.out.println("Error: " + error.getMessage());
                        }
                );
        return companyAccount;
    }
}

