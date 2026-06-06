package br.com.avcar.oficina.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI oficinaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Sistema de Gestão da Oficina Mecânica AV CAR AUTO CENTER")
                        .version("1.0.0")
                        .description("API REST monolítica para controle de clientes, veículos, ordens de serviço, peças, fornecedores, garantias, colaboradores, funções, serviços e pagamentos."));
    }
}
