package br.com.beca.ms_transacoes.infra.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cambio-client", url = "https://economia.awesomeapi.com.br")
public interface CambioClient {

    @GetMapping("/last/USD-BRL")
    CambioDTO buscarCotacaoDolar();
}