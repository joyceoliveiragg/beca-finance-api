package br.com.beca.ms_transacoes.infra.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "brasilapi-cambio",
        url = "https://brasilapi.com.br"
)
public interface CambioClient {

    @GetMapping("/api/exchangerate/v1/quotation/{moeda}")
    CambioDTO buscarCotacao(@PathVariable String moeda);
}
