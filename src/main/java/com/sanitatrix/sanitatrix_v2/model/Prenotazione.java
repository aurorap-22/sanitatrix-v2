package com.sanitatrix.sanitatrix_v2.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity @Table(name = "prenotazione")
public class Prenotazione {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime ora;

    @Column(nullable = false)
    private String stato;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_paziente")
    private Paziente paziente;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_medico")
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_prestazione")
    private Prestazione prestazione;

    @OneToOne(mappedBy = "prenotazione", cascade = CascadeType.ALL)
    private Referto referto;

    @OneToOne(mappedBy = "prenotazione", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    //GETTER/SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
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
