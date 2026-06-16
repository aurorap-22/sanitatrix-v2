package com.sanitatrix.sanitatrix_v2.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="medici")
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne @JoinColumn(name = "id_utente", nullable = false, unique = true)
    private Utente utente;

    @Column(unique = true, nullable = false, length = 16)
    private String codiceFiscale;

    private String nome, cognome, email;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Disponibilita> disponibilità;

    @OneToMany(mappedBy = "medico")
    private List<Prenotazione> prenotazioni;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private TipoVisita specializzazione;


    //GETTER/SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Disponibilita> getDisponibilità() {
        return disponibilità;
    }

    public void setDisponibilità(List<Disponibilita> disponibilità) {
        this.disponibilità = disponibilità;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public TipoVisita getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(TipoVisita specializzazione) {
        this.specializzazione = specializzazione;
    }
}
