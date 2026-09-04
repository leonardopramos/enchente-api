package br.com.api_do_tempo.enchente.controller.sincronizacao;

import br.com.api_do_tempo.enchente.dto.sincronizacao.SincronizacaoResultado;
import br.com.api_do_tempo.enchente.service.sincronizacao.SincronizacaoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/sincronizar")
@Tag(name = "Sincronização", description = "Atualização das estações meteorológicas armazenadas localmente")
public class SincronizacaoController {

    private static final Logger log = LoggerFactory.getLogger(SincronizacaoController.class);

    private final SincronizacaoService service;

    public SincronizacaoController(SincronizacaoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Sincroniza as estações meteorológicas",
            description = "Consulta a API hidrometeorológica externa e persiste as estações meteorológicas e seus dados brutos no banco de dados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sincronização concluída"),
            @ApiResponse(responseCode = "500", description = "Erro ao consultar a API externa ou persistir os dados")
    })
    public ResponseEntity<SincronizacaoResultado> sincronizar() {
        log.info("Requisição recebida para sincronização manual de estações meteorológicas");
        SincronizacaoResultado resultado = service.sincronizar();
        log.info("Sincronização manual concluída com sucesso: {} estações sincronizadas", resultado.estacoesSincronizadas());
        return ResponseEntity.ok(resultado);
    }
}
