package br.com.beca.ms_transacoes.infra.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "mock-contas", url = "SUA_URL_DO_MOCKAPI_AQUI/api/v1")
public interface ContasClient {

    @GetMapping("/contas/{id}")
    ContaMockDTO buscarConta(@PathVariable("id") Long id);

    @PutMapping("/contas/{id}")
    void atualizarSaldo(@PathVariable("id") Long id, @RequestBody ContaMockDTO dados);
}


