package br.com.beca.ms_processor.infra.feign;

import java.time.LocalDateTime;

public record CambioDTO(
        String currency,
        String name,
        Double bid,
        LocalDateTime timestamp
) {
    public CambioDTO(double v) {
        this("USD", "Dólar Americano", v, LocalDateTime.now());
    }

}
