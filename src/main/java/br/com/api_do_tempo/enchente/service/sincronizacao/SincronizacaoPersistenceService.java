package br.com.api_do_tempo.enchente.service.sincronizacao;

import br.com.api_do_tempo.enchente.dto.sincronizacao.GraphQlResponse;
import br.com.api_do_tempo.enchente.dto.sincronizacao.SincronizacaoResultado;
import br.com.api_do_tempo.enchente.entity.estacao.EstacaoMeteorologica;
import br.com.api_do_tempo.enchente.repository.estacao.EstacaoMeteorologicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SincronizacaoPersistenceService {

    private final EstacaoMeteorologicaRepository repository;

    public SincronizacaoPersistenceService(EstacaoMeteorologicaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public SincronizacaoResultado salvar(
            List<GraphQlResponse.EstacaoDto> estacoes, String dadosBrutos) {
        OffsetDateTime agora = OffsetDateTime.now(ZoneOffset.UTC);
        Map<String, EstacaoMeteorologica> existentes = new HashMap<>();
        repository.findAllByCodigoIn(estacoes.stream()
                        .map(GraphQlResponse.EstacaoDto::codigo)
                        .filter(codigo -> codigo != null && !codigo.isBlank())
                        .toList())
                .forEach(estacao -> existentes.put(estacao.getCodigo(), estacao));

        List<EstacaoMeteorologica> paraSalvar = estacoes.stream()
                .filter(estacao -> estacao.codigo() != null && !estacao.codigo().isBlank())
                .map(estacao -> atualizarEntidade(estacao, existentes, dadosBrutos, agora))
                .toList();
        repository.saveAll(paraSalvar);
        return new SincronizacaoResultado(paraSalvar.size(), agora);
    }

    private EstacaoMeteorologica atualizarEntidade(GraphQlResponse.EstacaoDto dto,
                                                   Map<String, EstacaoMeteorologica> existentes,
                                                   String dadosBrutos, OffsetDateTime agora) {
        EstacaoMeteorologica estacao = existentes.computeIfAbsent(dto.codigo(), EstacaoMeteorologica::new);
        GraphQlResponse.Name name = dto.name();
        GraphQlResponse.Position position = dto.position();
        GraphQlResponse.Relacao relacao = dto.filter() == null ? null : dto.filter().relacao();
        estacao.atualizar(name == null ? null : name.general(),
                position == null ? null : position.latitude(),
                position == null ? null : position.longitude(),
                position == null ? null : position.bacia(),
                position == null ? null : position.regiao(),
                position == null ? null : position.altitude(),
                relacao == null ? Boolean.FALSE : relacao.tem_nivel_do_rio(),
                dadosBrutos, agora);
        return estacao;
    }
}
