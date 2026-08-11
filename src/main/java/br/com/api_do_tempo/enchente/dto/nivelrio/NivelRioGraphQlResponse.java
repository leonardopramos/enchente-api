package br.com.api_do_tempo.enchente.dto.nivelrio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NivelRioGraphQlResponse(Data data, List<Error> errors) {
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Error(String message) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(TagsData tags_data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TagsData(List<TagData> qualle_meteorologia) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TagData(
            String codigo,
            Name name,
            Position position,
            StationData data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Name(String prefix, String general, String local) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Position(String bacia, String regiao, Double altitude) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record StationData(
            Rio rio,
            Chuva chuva,
            Temperatura temperatura,
            Umidade umidade,
            SensTermica senstermica,
            Vento vento) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Rio(Value<String> rio_nome, Value<Double> rio_nivel,
                      Value<Double> rio_nivel_tendencia, Value<Double> rio_area_drenagem) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Chuva(Acumulado acumulado) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Acumulado(
            Value<Double> s015, Value<Double> min005, Value<Double> min015,
            Value<Double> h001, Value<Double> h003, Value<Double> h006,
            Value<Double> h012, Value<Double> h024, Value<Double> h168) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Temperatura(Value<Double> atual) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Umidade(Value<Double> atual) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SensTermica(Value<Double> atual) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Vento(Value<Double> velocidade_media, Value<Double> velocidade_maxima) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Value<T>(T value) {}
}
