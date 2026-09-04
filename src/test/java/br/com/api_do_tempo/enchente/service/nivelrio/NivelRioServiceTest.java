package br.com.api_do_tempo.enchente.service.nivelrio;

import br.com.api_do_tempo.enchente.client.nivelrio.NivelRioClient;
import br.com.api_do_tempo.enchente.dto.nivelrio.NivelRioGraphQlResponse;
import br.com.api_do_tempo.enchente.dto.nivelrio.NivelRioResultado;
import br.com.api_do_tempo.enchente.dto.nivelrio.RioNivelTendenciaEnum;
import br.com.api_do_tempo.enchente.entity.estacao.EstacaoMeteorologica;
import br.com.api_do_tempo.enchente.repository.estacao.EstacaoMeteorologicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NivelRioServiceTest {

    @Mock
    private EstacaoMeteorologicaRepository repository;

    @Mock
    private NivelRioClient client;

    @InjectMocks
    private NivelRioService service;

    private EstacaoMeteorologica estacao;
    private NivelRioGraphQlResponse mockResponse;

    @BeforeEach
    void setUp() {
        estacao = new EstacaoMeteorologica("EST01");
        estacao.atualizar("Estacao Teste", -30.10, -51.22, "Bacia", "Regiao", 10.0, true, "{}", null);

        NivelRioGraphQlResponse.Value<Double> rioValue = new NivelRioGraphQlResponse.Value<>(3.5);
        NivelRioGraphQlResponse.Value<Double> tendencia = new NivelRioGraphQlResponse.Value<>(0.0);
        NivelRioGraphQlResponse.Rio rio = new NivelRioGraphQlResponse.Rio(null, rioValue, tendencia, null);
        NivelRioGraphQlResponse.StationData data = new NivelRioGraphQlResponse.StationData(rio, null, null, null, null, null);
        NivelRioGraphQlResponse.TagData qualle = new NivelRioGraphQlResponse.TagData(
                "EST01", null, null, data
        );
        NivelRioGraphQlResponse.TagsData tagsData = new NivelRioGraphQlResponse.TagsData(List.of(qualle));
        mockResponse = new NivelRioGraphQlResponse(new NivelRioGraphQlResponse.Data(tagsData), null);
    }

    @Test
    void deveBuscarPorCoordenadasEmStringComSucesso() {
        when(repository.findAll()).thenReturn(List.of(estacao));
        when(client.buscarNivel("EST01")).thenReturn(mockResponse);

        NivelRioResultado resultado = service.buscar("-30.10950136318255, -51.22996968944418");

        assertNotNull(resultado);
        assertEquals("EST01", resultado.codigo());
        assertEquals(3.5, resultado.rio().rio_nivel());
        assertEquals(RioNivelTendenciaEnum.ESTAVEL, resultado.rio().rio_nivel_tendencia_status());
        verify(client).buscarNivel("EST01");
    }

    @Test
    void deveBuscarPorCoordenadasSemEspacoAposVirgula() {
        when(repository.findAll()).thenReturn(List.of(estacao));
        when(client.buscarNivel("EST01")).thenReturn(mockResponse);

        NivelRioResultado resultado = service.buscar("-30.10950136318255,-51.22996968944418");

        assertNotNull(resultado);
        assertEquals("EST01", resultado.codigo());
    }

    @Test
    void deveLancarExcecaoQuandoCoordenadaForNulaOuVazia() {
        assertThrows(IllegalArgumentException.class, () -> service.buscar((String) null));
        assertThrows(IllegalArgumentException.class, () -> service.buscar(""));
        assertThrows(IllegalArgumentException.class, () -> service.buscar("   "));
    }

    @Test
    void deveLancarExcecaoQuandoFormatoNaoContiverDuasPartes() {
        assertThrows(IllegalArgumentException.class, () -> service.buscar("-30.10950136318255"));
        assertThrows(IllegalArgumentException.class, () -> service.buscar("-30.10,-51.22,-20.00"));
    }

    @Test
    void deveLancarExcecaoQuandoValoresNaoForemNumericos() {
        assertThrows(IllegalArgumentException.class, () -> service.buscar("abc, -51.22"));
        assertThrows(IllegalArgumentException.class, () -> service.buscar("-30.10, def"));
    }

    @Test
    void deveLancarExcecaoQuandoLatitudeOuLongitudeEstiveremForaDoIntervaloValido() {
        assertThrows(IllegalArgumentException.class, () -> service.buscar("-95.0, -51.22"));
        assertThrows(IllegalArgumentException.class, () -> service.buscar("-30.10, 185.0"));
    }

    @Test
    void deveClassificarTendenciaComoSubindoQuandoValorForPositivo() {
        NivelRioGraphQlResponse response = responseComTendencia(0.001);
        when(repository.findAll()).thenReturn(List.of(estacao));
        when(client.buscarNivel("EST01")).thenReturn(response);

        NivelRioResultado resultado = service.buscar(-30.10, -51.22);

        assertEquals(RioNivelTendenciaEnum.SUBINDO, resultado.rio().rio_nivel_tendencia_status());
    }

    @Test
    void deveClassificarTendenciaComoBaixandoQuandoValorForNegativo() {
        NivelRioGraphQlResponse response = responseComTendencia(-0.001);
        when(repository.findAll()).thenReturn(List.of(estacao));
        when(client.buscarNivel("EST01")).thenReturn(response);

        NivelRioResultado resultado = service.buscar(-30.10, -51.22);

        assertEquals(RioNivelTendenciaEnum.BAIXANDO, resultado.rio().rio_nivel_tendencia_status());
    }

    @Test
    void deveClassificarTendenciaComoSemDadosQuandoValorForNulo() {
        NivelRioGraphQlResponse response = responseComTendencia(null);
        when(repository.findAll()).thenReturn(List.of(estacao));
        when(client.buscarNivel("EST01")).thenReturn(response);

        NivelRioResultado resultado = service.buscar(-30.10, -51.22);

        assertEquals(RioNivelTendenciaEnum.SEM_DADOS, resultado.rio().rio_nivel_tendencia_status());
    }

    private NivelRioGraphQlResponse responseComTendencia(Double valorTendencia) {
        NivelRioGraphQlResponse.Value<Double> rioValue = new NivelRioGraphQlResponse.Value<>(3.5);
        NivelRioGraphQlResponse.Value<Double> tendencia = valorTendencia == null
                ? null
                : new NivelRioGraphQlResponse.Value<>(valorTendencia);
        NivelRioGraphQlResponse.Rio rio = new NivelRioGraphQlResponse.Rio(null, rioValue, tendencia, null);
        NivelRioGraphQlResponse.StationData data = new NivelRioGraphQlResponse.StationData(rio, null, null, null, null, null);
        NivelRioGraphQlResponse.TagData qualle = new NivelRioGraphQlResponse.TagData("EST01", null, null, data);
        NivelRioGraphQlResponse.TagsData tagsData = new NivelRioGraphQlResponse.TagsData(List.of(qualle));
        return new NivelRioGraphQlResponse(new NivelRioGraphQlResponse.Data(tagsData), null);
    }
}
