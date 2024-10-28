package org.example.api;

import org.example.api.crypto.service.CryptoService;
import org.example.common.crypto.dto.CryptoResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CryptoServiceTest {

    @Mock
    private WebClient mockWebClient;

    @InjectMocks
    private CryptoService cryptoService;

    @Test
    public void testGetCryptoValue() {
        WebClient.RequestHeadersUriSpec<?> request = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.get()).thenAnswer(invocation -> request);
        when(request.uri(anyString())).thenAnswer(invocation -> headersSpec);
        when(headersSpec.retrieve()).thenAnswer(invocation -> responseSpec);
        when(responseSpec.bodyToMono(CryptoResponse.class)).thenReturn(Mono.just(new CryptoResponse("crypto:BTC:timestamp:2024-10-25_11:47", "3530197.9594236473")));

        Mono<CryptoResponse> result = cryptoService.getCryptoValue("BTC", "2024-10-25", "11:47");

    }
}
