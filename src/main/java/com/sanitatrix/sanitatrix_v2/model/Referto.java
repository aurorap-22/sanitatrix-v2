package com.sanitatrix.sanitatrix_v2.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name = "referti")
public class Referto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_paziente", nullable = false)
    private Paziente paziente; // per storico veloce

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_prenotazione", nullable = false)
    private Prenotazione prenotazione;

    @Column(columnDefinition = "TEXT")
    private String diagnosi;

    @Column(columnDefinition = "TEXT")
    private String terapia;

    @Column(columnDefinition = "TEXT")
    private String esamiConsigliati;

    @Column(nullable = false)
    private LocalDate dataReferto= LocalDate.now();

    //GETTER/SETTER


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public String getDiagnosi() {
        return diagnosi;
    }

    public void setDiagnosi(String diagnosi) {
        this.diagnosi = diagnosi;
    }

    public String getTerapia() {
        return terapia;
    }

    public void setTerapia(String terapia) {
        this.terapia = terapia;
    }

    public String getEsamiConsigliati() {
        return esamiConsigliati;
    }

    public void setEsamiConsigliati(String esamiConsigliati) {
        this.esamiConsigliati = esamiConsigliati;
    }

    public LocalDate getDataReferto() {
        return dataReferto;
    }

    public void setDataReferto(LocalDate dataReferto) {
        this.dataReferto = dataReferto;
    }
}
