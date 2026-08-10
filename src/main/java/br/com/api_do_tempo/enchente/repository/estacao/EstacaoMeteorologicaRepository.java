package br.com.api_do_tempo.enchente.repository.estacao;

import br.com.api_do_tempo.enchente.entity.estacao.EstacaoMeteorologica;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EstacaoMeteorologicaRepository extends JpaRepository<EstacaoMeteorologica, String> {
    List<EstacaoMeteorologica> findAllByCodigoIn(Collection<String> codigos);
}
