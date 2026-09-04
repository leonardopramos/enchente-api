package br.com.api_do_tempo.enchente.dto.nivelrio;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados da estação meteorológica mais próxima e do nível do rio")
public record NivelRioResultado(
        String codigo,
        NivelRioGraphQlResponse.Name name,
        NivelRioGraphQlResponse.Position position,
        RioResultado rio,
        NivelRioGraphQlResponse.Chuva chuva,
        NivelRioGraphQlResponse.Temperatura temperatura,
        NivelRioGraphQlResponse.Umidade umidade,
        NivelRioGraphQlResponse.SensTermica senstermica,
        NivelRioGraphQlResponse.Vento vento) {
}
