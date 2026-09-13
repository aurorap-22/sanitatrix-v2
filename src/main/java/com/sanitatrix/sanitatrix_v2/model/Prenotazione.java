package com.sanitatrix.sanitatrix_v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prenotazioni")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="stato")
    private String stato;

    @ManyToOne
    @JoinColumn(name = "paziente_id", nullable = false)
    @JsonIgnoreProperties({"prenotazioni", "referti", "utente"})
    private Paziente paziente;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonIgnoreProperties({"prenotazioni"})
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "prestazione_id", nullable = false)
    private Prestazione prestazione;

    @Column(name = "data_ora", nullable = false)
    private LocalDateTime dataOra;

    @Column(name = "data_fine")
    private LocalDateTime dataFine;

    @OneToOne(mappedBy = "prenotazione", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"prenotazione"})
    private Referto referto;

    public Prenotazione(){}

    // GETTER E SETTER
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public Prestazione getPrestazione() { return prestazione; }
    public void setPrestazione(Prestazione prestazione) { this.prestazione = prestazione; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public LocalDateTime getDataFine() { return dataFine; }
    public void setDataFine(LocalDateTime dataFine) { this.dataFine = dataFine; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public Referto getReferto() { return referto; }
    public void setReferto(Referto referto) { this.referto = referto; }
}