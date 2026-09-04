package br.com.api_do_tempo.enchente.dto.nivelrio;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Classificação do sinal de tendência do nível do rio")
public enum RioNivelTendenciaEnum {
    BAIXANDO(-1),
    ESTAVEL(0),
    SUBINDO(1),
    SEM_DADOS(null);

    private final Integer codigo;

    RioNivelTendenciaEnum(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public static RioNivelTendenciaEnum fromApiValue(Double value) {
        if (value == null) {
            return SEM_DADOS;
        }
        int comparacao = Double.compare(value, 0D);
        if (comparacao < 0) {
            return BAIXANDO;
        }
        if (comparacao > 0) {
            return SUBINDO;
        }
        return ESTAVEL;
    }
}
