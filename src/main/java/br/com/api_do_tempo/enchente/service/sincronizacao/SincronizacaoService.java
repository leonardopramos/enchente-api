package br.com.api_do_tempo.enchente.service.sincronizacao;

import br.com.api_do_tempo.enchente.client.sincronizacao.SincronizacaoClient;
import br.com.api_do_tempo.enchente.dto.sincronizacao.GraphQlResponse;
import br.com.api_do_tempo.enchente.dto.sincronizacao.SincronizacaoResultado;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SincronizacaoService {

    private static final Logger log = LoggerFactory.getLogger(SincronizacaoService.class);

    private final SincronizacaoClient client;
    private final SincronizacaoPersistenceService persistenceService;
    private final ObjectMapper objectMapper;

    public SincronizacaoService(SincronizacaoClient client,
                                SincronizacaoPersistenceService persistenceService,
                                ObjectMapper objectMapper) {
        this.client = client;
        this.persistenceService = persistenceService;
        this.objectMapper = objectMapper;
    }

    @Scheduled(cron = "${app.synchronization.cron:0 0 3 * * *}", zone = "${app.synchronization.zone:America/Sao_Paulo}")
    public void sincronizarAgendado() {
        log.info("Iniciando execução da sincronização agendada de estações meteorológicas");
        sincronizar();
    }

    public synchronized SincronizacaoResultado sincronizar() {
        GraphQlResponse response = client.buscarEstacoes();
        List<GraphQlResponse.EstacaoDto> estacoes = response.data().tags_data().qualle_meteorologia();
        if (estacoes == null) {
            estacoes = List.of();
        }
        log.info("Recebidas {} estações da API externa para processamento", estacoes.size());

        String dadosBrutos = serializar(response);
        return persistenceService.salvar(estacoes, dadosBrutos);
    }

    private String serializar(GraphQlResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Não foi possível serializar a resposta da API", exception);
        }
    }
}
