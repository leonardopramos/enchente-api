package br.com.api_do_tempo.enchente.controller.nivelrio;

import br.com.api_do_tempo.enchente.dto.nivelrio.NivelRioResultado;
import br.com.api_do_tempo.enchente.service.nivelrio.NivelRioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NivelRioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NivelRioService service;

    @InjectMocks
    private NivelRioController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void deveBuscarNivelDoRioComParametroCoordenadas() throws Exception {
        String coordenadas = "-30.10950136318255, -51.22996968944418";
        NivelRioResultado resultado = new NivelRioResultado("EST01", null, null, null, null, null, null, null, null);

        when(service.buscar(coordenadas)).thenReturn(resultado);

        mockMvc.perform(get("/nivel-rio")
                        .param("coordenadas", coordenadas))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("EST01"));

        verify(service).buscar(coordenadas);
    }
}
