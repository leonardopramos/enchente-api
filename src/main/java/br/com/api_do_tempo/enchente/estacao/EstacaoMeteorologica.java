package br.com.api_do_tempo.enchente.estacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "estacao_meteorologica")
public class EstacaoMeteorologica {

    @Id
    @Column(name = "codigo", nullable = false, length = 100)
    private String codigo;

    @Column(name = "nome", length = 255)
    private String nome;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "bacia", length = 255)
    private String bacia;

    @Column(name = "regiao", length = 255)
    private String regiao;

    @Column(name = "altitude")
    private Double altitude;

    @Column(name = "tem_nivel_do_rio", nullable = false)
    private Boolean temNivelDoRio;

    @Column(name = "dados_brutos", nullable = false, columnDefinition = "TEXT")
    private String dadosBrutos;

    @Column(name = "sincronizado_em", nullable = false)
    private OffsetDateTime sincronizadoEm;

    protected EstacaoMeteorologica() {
    }

    public EstacaoMeteorologica(String codigo) {
        this.codigo = codigo;
    }

    public void atualizar(String nome, Double latitude, Double longitude, String bacia,
                          String regiao, Double altitude, Boolean temNivelDoRio,
                          String dadosBrutos, OffsetDateTime sincronizadoEm) {
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bacia = bacia;
        this.regiao = regiao;
        this.altitude = altitude;
        this.temNivelDoRio = Boolean.TRUE.equals(temNivelDoRio);
        this.dadosBrutos = dadosBrutos;
        this.sincronizadoEm = sincronizadoEm;
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getBacia() { return bacia; }
    public String getRegiao() { return regiao; }
    public Double getAltitude() { return altitude; }
    public Boolean getTemNivelDoRio() { return temNivelDoRio; }
    public String getDadosBrutos() { return dadosBrutos; }
    public OffsetDateTime getSincronizadoEm() { return sincronizadoEm; }
}
