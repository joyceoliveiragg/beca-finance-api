package br.com.beca.ms_transacoes.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Transações (MS-Transacoes)")
                        .version("1.0")
                        .description("Processamento assíncrono de pagamentos via Kafka."));
    }
}