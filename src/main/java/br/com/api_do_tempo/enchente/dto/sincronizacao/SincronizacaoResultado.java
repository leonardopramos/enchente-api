package br.com.api_do_tempo.enchente.dto.sincronizacao;

import java.time.OffsetDateTime;

public record SincronizacaoResultado(int estacoesSincronizadas, OffsetDateTime sincronizadoEm) {
}
