package com.sanitatrix.sanitatrix_v2.model;
import jakarta.persistence.*;
import java.util.List;

@Entity @Table(name = "pazienti")
public class Paziente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne @JoinColumn(name = "id_utente", nullable = false, unique = true)
    private Utente utente;

    @Column(unique = true, nullable = false, length = 16)
    private String codiceFiscale;
    private String nome, cognome, telefono, email;

    @OneToMany(mappedBy = "paziente")
    private List<Prenotazione> prenotazioni;

    @OneToMany(mappedBy = "paziente")
    private List<Referto> referti;

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
