package br.com.api_do_tempo.enchente.dto.nivelrio;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do rio retornados pela API hidrometeorológica")
public record RioResultado(
        String rio_nome,
        Double rio_nivel,
        Double rio_nivel_tendencia,
        RioNivelTendenciaEnum rio_nivel_tendencia_status,
        Double rio_area_drenagem) {

    public static RioResultado from(NivelRioGraphQlResponse.Rio rio) {
        if (rio == null) {
            return null;
        }
        Double tendencia = rio.rio_nivel_tendencia() != null ? rio.rio_nivel_tendencia().value() : null;
        return new RioResultado(
                rio.rio_nome() != null ? rio.rio_nome().value() : null,
                rio.rio_nivel() != null ? rio.rio_nivel().value() : null,
                tendencia,
                RioNivelTendenciaEnum.fromApiValue(tendencia),
                rio.rio_area_drenagem() != null ? rio.rio_area_drenagem().value() : null
        );
    }
}
