package br.com.api_do_tempo.enchente.client.nivelrio;

import br.com.api_do_tempo.enchente.dto.nivelrio.NivelRioGraphQlResponse;
import br.com.api_do_tempo.enchente.dto.sincronizacao.GraphQlRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NivelRioClient {

    private static final String QUERY = "query {\n"
            + "  tags_data(station: [\"%s\"], clients: [\"casa-militar-defesa-civil-rs\"]) {\n"
            + "    qualle_meteorologia {\n"
            + "      codigo\n"
            + "      name { prefix general local }\n"
            + "      position { bacia regiao altitude }\n"
            + "      data {\n"
            + "        rio { rio_nome { value } rio_nivel { value } rio_nivel_tendencia { value } rio_area_drenagem { value } }\n"
            + "        chuva { acumulado { s015 { value } min005 { value } min015 { value } h001 { value } h003 { value } h006 { value } h012 { value } h024 { value } h168 { value } } }\n"
            + "        temperatura { atual { value } }\n"
            + "        umidade { atual { value } }\n"
            + "        senstermica { atual { value } }\n"
            + "        vento { velocidade_media { value } velocidade_maxima { value } }\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "}";

    private final RestClient restClient;

    public NivelRioClient(RestClient.Builder builder,
                          @Value("${app.synchronization.url:https://redehidrometeorologica.defesacivil.rs.gov.br/graphql}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    public NivelRioGraphQlResponse buscarNivel(String codigoEstacao) {
        String query = QUERY.formatted(codigoEstacao.replace("\"", "\\\""));
        NivelRioGraphQlResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GraphQlRequest(query))
                .retrieve()
                .body(NivelRioGraphQlResponse.class);

        if (response == null || response.hasErrors()) {
            String message = response != null && response.errors() != null
                    ? response.errors().stream().map(NivelRioGraphQlResponse.Error::message).findFirst().orElse("Resposta inválida")
                    : "A API externa retornou uma resposta vazia";
            throw new IllegalStateException("Erro na API GraphQL: " + message);
        }
        return response;
    }
}
