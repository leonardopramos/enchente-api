package br.com.api_do_tempo.enchente.dto.sincronizacao;

import java.time.OffsetDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado da sincronização das estações meteorológicas")
public record SincronizacaoResultado(
        @Schema(description = "Quantidade de estações sincronizadas", example = "42", minimum = "0") int estacoesSincronizadas,
        @Schema(description = "Data e hora em que a sincronização foi concluída", example = "2026-08-10T12:30:00-03:00") OffsetDateTime sincronizadoEm) {
}
