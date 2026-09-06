package com.sanitatrix.sanitatrix_v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="medici")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(nullable = false)
   private String nome;

   @Column(nullable = false)
   private String cognome;

   @Enumerated(EnumType.STRING)@Column(nullable = false)
    private TipoVisita specializzazione;

   @Column
    private String telefono;

   @OneToOne
   @JoinColumn(name = "id_utente", nullable = false, unique = true)
   @JsonIgnoreProperties({"password", "codiceFiscale", "medico", "paziente"})
    private Utente utente;

   @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnore
    private List<Prenotazione> prenotazioni= new ArrayList<>();

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disponibilita> disponibilita= new ArrayList<>();

    //COSTRUTTORI
    public Medico(){}

    public Medico(String nome, String cognome, TipoVisita specializzazione, Utente utente) {
        this.nome = nome;
        this.cognome = cognome;
        this.specializzazione = specializzazione;
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

    public TipoVisita getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(TipoVisita specializzazione) {
        this.specializzazione = specializzazione;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public List<Disponibilita> getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(List<Disponibilita> disponibilita) {
        this.disponibilita = disponibilita;
    }
}
