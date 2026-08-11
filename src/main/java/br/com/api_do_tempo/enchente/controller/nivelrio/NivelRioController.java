package br.com.api_do_tempo.enchente.controller.nivelrio;

import br.com.api_do_tempo.enchente.dto.nivelrio.NivelRioResultado;
import br.com.api_do_tempo.enchente.service.nivelrio.NivelRioService;
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
            @ApiResponse(responseCode = "400", description = "Latitude ou longitude inválida"),
            @ApiResponse(responseCode = "404", description = "Nenhuma estação ou nível do rio disponível para a localização informada"),
            @ApiResponse(responseCode = "500", description = "Erro ao consultar o nível do rio")
    })
    public ResponseEntity<NivelRioResultado> buscar(@RequestParam
                                                     @Parameter(description = "Latitude da localização de consulta", example = "-29.6868",
                                                             schema = @Schema(type = "number", format = "double", minimum = "-90", maximum = "90")) Double latitude,
                                                     @RequestParam
                                                     @Parameter(description = "Longitude da localização de consulta", example = "-51.1328",
                                                             schema = @Schema(type = "number", format = "double", minimum = "-180", maximum = "180")) Double longitude) {
        return ResponseEntity.ok(service.buscar(latitude, longitude));
    }
}
