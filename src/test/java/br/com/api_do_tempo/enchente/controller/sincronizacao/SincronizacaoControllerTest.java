package br.com.api_do_tempo.enchente.controller.sincronizacao;

import br.com.api_do_tempo.enchente.dto.sincronizacao.SincronizacaoResultado;
import br.com.api_do_tempo.enchente.service.sincronizacao.SincronizacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SincronizacaoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SincronizacaoService service;

    @InjectMocks
    private SincronizacaoController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void deveExecutarSincronizacaoComSucesso() throws Exception {
        OffsetDateTime agora = OffsetDateTime.of(2026, 8, 25, 22, 0, 0, 0, ZoneOffset.UTC);
        SincronizacaoResultado resultado = new SincronizacaoResultado(15, agora);

        when(service.sincronizar()).thenReturn(resultado);

        mockMvc.perform(post("/sincronizar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estacoesSincronizadas").value(15));

        verify(service).sincronizar();
    }
}
