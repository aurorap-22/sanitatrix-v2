package com.sanitatrix.sanitatrix_v2.model;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "pazienti")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Paziente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private LocalDate dataNascita;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String indirizzo;

    @OneToOne @JoinColumn(name = "id_utente", nullable = false, unique = true)
    private Utente utente;

    @OneToMany(mappedBy = "paziente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prenotazione> prenotazioni= new ArrayList<>();

    @OneToMany(mappedBy = "paziente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Referto> referti= new ArrayList<>();

    //COSTRUTTORI
    public Paziente(){}

    public Paziente(String nome, String cognome, LocalDate dataNascita, String telefono, Utente utente) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.utente= utente;
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public List<Referto> getReferti() {
        return referti;
    }

    public void setReferti(List<Referto> referti) {
        this.referti = referti;
    }
}
