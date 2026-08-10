package br.com.api_do_tempo.enchente.client.sincronizacao;

import br.com.api_do_tempo.enchente.dto.sincronizacao.GraphQlRequest;
import br.com.api_do_tempo.enchente.dto.sincronizacao.GraphQlResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SincronizacaoClient {

    private static final String QUERY = "query {\n"
            + "  tags_data(clients: [\"casa-militar-defesa-civil-rs\"]) {\n"
            + "    qualle_meteorologia {\n"
            + "      codigo\n"
            + "      name { general }\n"
            + "      position { latitude longitude bacia regiao altitude }\n"
            + "      filter { relacao { tem_nivel_do_rio } }\n"
            + "    }\n"
            + "  }\n"
            + "}";

    private final RestClient restClient;

    public SincronizacaoClient(RestClient.Builder builder,
                               @Value("${app.synchronization.url:https://redehidrometeorologica.defesacivil.rs.gov.br/graphql}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    public GraphQlResponse buscarEstacoes() {
        GraphQlResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GraphQlRequest(QUERY))
                .retrieve()
                .body(GraphQlResponse.class);

        if (response == null) {
            throw new IllegalStateException("A API externa retornou uma resposta vazia");
        }
        if (response.hasErrors()) {
            String message = response.errors().stream()
                    .map(GraphQlResponse.GraphQlError::message)
                    .findFirst()
                    .orElse("Erro não informado pela API GraphQL");
            throw new IllegalStateException("Erro na API GraphQL: " + message);
        }
        if (response.data() == null || response.data().tags_data() == null) {
            throw new IllegalStateException("A API GraphQL retornou dados inválidos");
        }
        return response;
    }
}
