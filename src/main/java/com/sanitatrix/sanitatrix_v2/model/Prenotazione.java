package com.sanitatrix.sanitatrix_v2.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity @Table(name = "prenotazioni")
public class Prenotazione {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataOra;

    @Column(nullable = false)
    private LocalDateTime dataFine;


    @Column(nullable = false)
    private String stato;//PRENOTATA, COMPLETATA, ANNULLATA

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_paziente")
    private Paziente paziente;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_medico")
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_prestazione")
    private Prestazione prestazione;

    @OneToOne(mappedBy = "prenotazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private Referto referto;

    @OneToOne(mappedBy = "prenotazione", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pagamento pagamento;

   //COSTRUTTORI
    public Prenotazione(){}

    public Prenotazione(LocalDateTime dataOra, String stato, Paziente paziente, Medico medico, Prestazione prestazione) {
        this.dataOra = dataOra;
        this.stato = stato;
        this.paziente = paziente;
        this.medico = medico;
        this.prestazione = prestazione;
    }
    //GETTER/SETTER


    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Prestazione getPrestazione() {
        return prestazione;
    }

    public void setPrestazione(Prestazione prestazione) {
        this.prestazione = prestazione;
    }

    public Referto getReferto() {
        return referto;
    }

    public void setReferto(Referto referto) {
        this.referto = referto;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    //METODO PER VERIFICARE SE IL MEDICO PUO' FARE UNA DETERMINATA VISITA


}
