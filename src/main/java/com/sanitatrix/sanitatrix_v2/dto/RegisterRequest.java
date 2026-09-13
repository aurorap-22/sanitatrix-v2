package com.sanitatrix.sanitatrix_v2.dto;

import com.sanitatrix.sanitatrix_v2.model.Ruolo;
import com.sanitatrix.sanitatrix_v2.model.TipoVisita;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String codiceFiscale;
    private Ruolo ruolo;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String telefono;
    private String indirizzo;
    private TipoVisita specializzazione;
}
