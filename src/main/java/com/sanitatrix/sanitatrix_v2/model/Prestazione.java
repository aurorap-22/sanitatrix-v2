package com.sanitatrix.sanitatrix_v2.model;
import jakarta.persistence.*;
import  java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "prestazioni")
public class Prestazione {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING) @Column(nullable = false, unique = true)
    private TipoVisita tipoVisita;

    @Column(length = 500)
    private String descrizione;

    @Column(nullable = false, precision=10, scale = 2)
    private BigDecimal prezzo;

    @OneToMany(mappedBy = "prestazione", cascade = CascadeType.ALL)
    private List<Prenotazione> prenotazioni= new ArrayList<>();

    //COSTRUTTORI
    public Prestazione(){}

    public Prestazione(String nome, TipoVisita tipoVisita, String descrizione, BigDecimal prezzo) {
        this.nome = nome;
        this.tipoVisita = tipoVisita;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
    }
    //GETTER/SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoVisita getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(TipoVisita tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }
}
