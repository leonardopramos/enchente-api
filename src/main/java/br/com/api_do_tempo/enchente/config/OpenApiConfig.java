package br.com.api_do_tempo.enchente.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Monitoramento de Enchentes",
                version = "v1",
                description = "API para sincronização de estações meteorológicas e consulta do nível dos rios."
        )
)
public class OpenApiConfig {
}
