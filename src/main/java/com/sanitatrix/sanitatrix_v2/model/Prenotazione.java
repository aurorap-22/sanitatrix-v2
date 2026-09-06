package com.sanitatrix.sanitatrix_v2.model;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Entity @Table(name = "prenotazioni")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Prenotazione {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataOra;

    @Column(nullable = false)
    private LocalDateTime dataFine;

    @Column(nullable = false)
    private String stato;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "id_paziente")
    private Paziente paziente;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "id_medico")
    private Medico medico;

    @ManyToOne(fetch = FetchType.EAGER, optional = true) @JoinColumn(name = "id_prestazione", nullable = true)
    private Prestazione prestazione;

    // --- AGGIUNTA FONDAMENTALE ---
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_visita")
    private TipoVisita tipoVisita;

    @OneToOne(mappedBy = "prenotazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private Referto referto;

    @OneToOne(mappedBy = "prenotazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pagamento pagamento;

    public Prenotazione(){}

    // GETTER SETTER
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }
    public LocalDateTime getDataFine() { return dataFine; }
    public void setDataFine(LocalDateTime dataFine) { this.dataFine = dataFine; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public Prestazione getPrestazione() { return prestazione; }
    public void setPrestazione(Prestazione prestazione) { this.prestazione = prestazione; }
    public TipoVisita getTipoVisita() { return tipoVisita; }
    public void setTipoVisita(TipoVisita tipoVisita) { this.tipoVisita = tipoVisita; }
    public Referto getReferto() { return referto; }
    public void setReferto(Referto referto) { this.referto = referto; }
    public Pagamento getPagamento() { return pagamento; }
    public void setPagamento(Pagamento pagamento) { this.pagamento = pagamento; }
}