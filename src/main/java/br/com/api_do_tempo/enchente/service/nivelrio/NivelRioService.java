package br.com.api_do_tempo.enchente.service.nivelrio;

import br.com.api_do_tempo.enchente.client.nivelrio.NivelRioClient;
import br.com.api_do_tempo.enchente.dto.nivelrio.NivelRioGraphQlResponse;
import br.com.api_do_tempo.enchente.dto.nivelrio.NivelRioResultado;
import br.com.api_do_tempo.enchente.dto.nivelrio.RioResultado;
import br.com.api_do_tempo.enchente.entity.estacao.EstacaoMeteorologica;
import br.com.api_do_tempo.enchente.repository.estacao.EstacaoMeteorologicaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NivelRioService {

    private static final Logger log = LoggerFactory.getLogger(NivelRioService.class);

    private final EstacaoMeteorologicaRepository repository;
    private final NivelRioClient client;

    public NivelRioService(EstacaoMeteorologicaRepository repository, NivelRioClient client) {
        this.repository = repository;
        this.client = client;
    }

    public NivelRioResultado buscar(String coordenadas) {
        if (coordenadas == null || coordenadas.isBlank()) {
            throw new IllegalArgumentException("Coordenadas não informadas");
        }
        String[] partes = coordenadas.split(",");
        if (partes.length != 2) {
            throw new IllegalArgumentException("Formato de coordenadas inválido. Utilize o formato: latitude,longitude");
        }
        try {
            Double latitude = Double.parseDouble(partes[0].trim());
            Double longitude = Double.parseDouble(partes[1].trim());
            return buscar(latitude, longitude);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Latitude ou longitude inválida", e);
        }
    }

    public NivelRioResultado buscar(Double latitude, Double longitude) {
        validarCoordenadas(latitude, longitude);
        EstacaoMeteorologica estacao = repository.findAll().stream()
                .filter(item -> item.getLatitude() != null && item.getLongitude() != null)
                .min((a, b) -> Double.compare(distancia(latitude, longitude, a), distancia(latitude, longitude, b)))
                .orElseThrow(() -> new EstacaoNaoEncontradaException("Nenhuma estação com coordenadas foi encontrada"));

        log.info("Estação mais próxima identificada: '{}' (código={})", estacao.getNome(), estacao.getCodigo());

        NivelRioGraphQlResponse response = client.buscarNivel(estacao.getCodigo());
        NivelRioGraphQlResponse.TagData tag = response.data() == null
                || response.data().tags_data() == null
                || response.data().tags_data().qualle_meteorologia() == null
                ? null : response.data().tags_data().qualle_meteorologia().stream().findFirst().orElse(null);
        if (tag == null || tag.data() == null || tag.data().rio() == null
                || tag.data().rio().rio_nivel() == null) {
            throw new EstacaoNaoEncontradaException("Não há nível do rio disponível para a estação mais próxima");
        }

        return new NivelRioResultado(tag.codigo(), tag.name(), tag.position(), RioResultado.from(tag.data().rio()),
                tag.data().chuva(), tag.data().temperatura(), tag.data().umidade(),
                tag.data().senstermica(), tag.data().vento());
    }

    private void validarCoordenadas(Double latitude, Double longitude) {
        if (latitude == null || longitude == null || !Double.isFinite(latitude) || !Double.isFinite(longitude)
                || latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Latitude ou longitude inválida");
        }
    }

    private double distancia(Double latitude, Double longitude, EstacaoMeteorologica estacao) {
        double latitudeEstacao = Math.toRadians(estacao.getLatitude());
        double longitudeEstacao = Math.toRadians(estacao.getLongitude());
        double deltaLatitude = latitudeEstacao - Math.toRadians(latitude);
        double deltaLongitude = longitudeEstacao - Math.toRadians(longitude);
        double seno = Math.pow(Math.sin(deltaLatitude / 2), 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(latitudeEstacao)
                * Math.pow(Math.sin(deltaLongitude / 2), 2);
        return 2 * Math.atan2(Math.sqrt(seno), Math.sqrt(1 - seno));
    }

    public static class EstacaoNaoEncontradaException extends RuntimeException {
        public EstacaoNaoEncontradaException(String message) {
            super(message);
        }
    }
}
