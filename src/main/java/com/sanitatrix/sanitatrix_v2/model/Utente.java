package com.sanitatrix.sanitatrix_v2.model;
import jakarta.persistence.*;



@Entity @Table(name = "utenti")
public class Utente {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 16)
    private String codiceFiscale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo;

    @OneToOne(mappedBy="utente", cascade =CascadeType.ALL)
    private Medico medico;

    @OneToOne(mappedBy="utente", cascade =CascadeType.ALL)
    private Paziente paziente;

    //COSTRUTTORI
    public Utente(){}
    public Utente(String email, String password, String codiceFiscale, Ruolo ruolo) {
        this.email = email;
        this.password = password;
        this.codiceFiscale = codiceFiscale;
        this.ruolo = ruolo;
    }

    //GETTER/SETTER


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }
}