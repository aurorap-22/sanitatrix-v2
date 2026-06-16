package com.sanitatrix.sanitatrix_v2.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name = "pagamenti")
public class Pagamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_prenotazione", nullable = false, unique = true)
    private Prenotazione prenotazione;

    @Column(nullable = false)
    private Double importo;

    private String metodoPagamento;//CONTANTI/ CARTA/ BONIFICO

    @Column(nullable = false)
    private String stato; //PAGATO/ NON PAGATO/ RIMBORSATO

    private LocalDate dataPagamento;

    //GETTER/SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
