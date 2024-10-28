package org.example.api.crypto.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.crypto.service.CryptoService;
import org.example.common.crypto.dto.CryptoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CryptoController {

    private final CryptoService cryptoService;

    @GetMapping("/crypto-value")
    public Mono<CryptoResponse> getCryptoValue(
            @RequestParam String coin,
            @RequestParam String date,
            @RequestParam String time) {
        return cryptoService.getCryptoValue(coin, date, time);
    }
}
