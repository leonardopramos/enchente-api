package br.com.api_do_tempo.enchente.controller.sincronizacao;

import br.com.api_do_tempo.enchente.dto.sincronizacao.SincronizacaoResultado;
import br.com.api_do_tempo.enchente.service.sincronizacao.SincronizacaoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sincronizar")
public class SincronizacaoController {

    private final SincronizacaoService service;

    public SincronizacaoController(SincronizacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SincronizacaoResultado> sincronizar() {
        return ResponseEntity.ok(service.sincronizar());
    }
}
