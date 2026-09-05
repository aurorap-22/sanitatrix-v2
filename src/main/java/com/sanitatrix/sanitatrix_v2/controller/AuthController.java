package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Medico;
import com.sanitatrix.sanitatrix_v2.model.Paziente;
import com.sanitatrix.sanitatrix_v2.model.Ruolo;
import com.sanitatrix.sanitatrix_v2.model.TipoVisita;
import com.sanitatrix.sanitatrix_v2.model.Utente;
import com.sanitatrix.sanitatrix_v2.repository.MedicoRepository;
import com.sanitatrix.sanitatrix_v2.repository.PazienteRepository;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    UtenteRepository utenteRepository;
    @Autowired
    PazienteRepository pazienteRepository;
    @Autowired
    MedicoRepository medicoRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String password = payload.get("password");
            String codiceFiscale = payload.get("codiceFiscale");
            String nome = payload.get("nome");
            String cognome = payload.get("cognome");
            String roleStr = payload.get("role").toUpperCase();

            if (utenteRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("Email già usata");
            }

            Utente utente = new Utente();
            utente.setEmail(email);
            utente.setPassword(passwordEncoder.encode(password));
            utente.setCodiceFiscale(codiceFiscale.toUpperCase());
            utente.setRuolo(Ruolo.valueOf(roleStr));
            utenteRepository.save(utente);

            if ("PAZIENTE".equals(roleStr)) {
                Paziente p = new Paziente();
                p.setUtente(utente);
                p.setNome(nome);
                p.setCognome(cognome);
                p.setDataNascita(LocalDate.of(2000, 1, 1));
                p.setTelefono("3330000000");
                p.setIndirizzo("Via Roma 1, Napoli");
                pazienteRepository.save(p);
            } else {
                Medico m = new Medico();
                m.setUtente(utente);
                m.setNome(nome);
                m.setCognome(cognome);
                m.setTelefono("3330000000");
                m.setSpecializzazione(TipoVisita.values()[0]);
                medicoRepository.save(m);
            }

            return ResponseEntity.ok("Registrato OK");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Errore: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        var opt = utenteRepository.findByEmail(email);
        if (opt.isEmpty() ||!passwordEncoder.matches(password, opt.get().getPassword())) {
            return ResponseEntity.status(401).body("Credenziali errate");
        }
        Utente u = opt.get();
        Map<String, Object> resp = new HashMap<>();
        resp.put("email", u.getEmail());
        resp.put("ruolo", u.getRuolo().toString());
        resp.put("id", u.getId());
        return ResponseEntity.ok(resp);
    }
}