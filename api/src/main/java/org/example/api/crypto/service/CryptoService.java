package org.example.api.crypto.service;

import lombok.RequiredArgsConstructor;
import org.example.common.crypto.dto.CryptoResponse;
import org.example.common.crypto.entity.Crypto;
import org.example.common.crypto.repository.CryptoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CryptoService {

    private final CryptoRepository cryptoRepository;
    private final WebClient webClient;

    public Mono<CryptoResponse> getCryptoValue(String coin, String date, String time) {
        String url = "http://3.36.61.198:8080/get-crypto-value?coin=" + coin + "&date=" + date + "&time=" + time;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CryptoResponse.class);
    }
}
