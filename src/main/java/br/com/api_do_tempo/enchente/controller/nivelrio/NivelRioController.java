package br.com.api_do_tempo.enchente.controller.nivelrio;

import br.com.api_do_tempo.enchente.dto.nivelrio.NivelRioResultado;
import br.com.api_do_tempo.enchente.service.nivelrio.NivelRioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/nivel-rio")
@Tag(name = "Nível do rio", description = "Consulta do nível do rio na estação meteorológica mais próxima")
public class NivelRioController {

    private static final Logger log = LoggerFactory.getLogger(NivelRioController.class);

    private final NivelRioService service;

    public NivelRioController(NivelRioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
            summary = "Consulta o nível do rio",
            description = "Localiza a estação meteorológica mais próxima das coordenadas informadas e consulta o nível atual do rio associado a ela."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nível do rio encontrado"),
            @ApiResponse(responseCode = "400", description = "Coordenadas inválidas"),
            @ApiResponse(responseCode = "404", description = "Nenhuma estação ou nível do rio disponível para a localização informada"),
            @ApiResponse(responseCode = "500", description = "Erro ao consultar o nível do rio")
    })
    public ResponseEntity<NivelRioResultado> buscar(@RequestParam
                                                     @Parameter(description = "Coordenadas da localização de consulta no formato 'latitude,longitude'",
                                                             example = "-30.10950136318255, -51.22996968944418",
                                                             schema = @Schema(type = "string")) String coordenadas) {
        log.info("Requisição recebida para consulta de nível do rio com coordenadas: '{}'", coordenadas);
        NivelRioResultado resultado = service.buscar(coordenadas);
        log.info("Consulta de nível do rio concluída com sucesso para a estação '{}' (código={})",
                resultado.name() != null ? resultado.name().general() : "-", resultado.codigo());
        return ResponseEntity.ok(resultado);
    }
}
