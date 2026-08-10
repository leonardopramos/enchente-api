package br.com.api_do_tempo.enchente.sincronizacao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GraphQlResponse(GraphQlData data, List<GraphQlError> errors) {
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GraphQlError(String message) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GraphQlData(TagsData tags_data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TagsData(List<EstacaoDto> qualle_meteorologia) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EstacaoDto(String codigo, Name name, Position position, Filter filter) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Name(String general) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Position(Double latitude, Double longitude, String bacia, String regiao, Double altitude) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Filter(Relacao relacao) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Relacao(Boolean tem_nivel_do_rio) {}
}
