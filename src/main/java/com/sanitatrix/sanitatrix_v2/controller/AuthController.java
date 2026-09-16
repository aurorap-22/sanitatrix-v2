package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.dto.RegisterRequest;
import com.sanitatrix.sanitatrix_v2.model.Medico;
import com.sanitatrix.sanitatrix_v2.model.Paziente;
import com.sanitatrix.sanitatrix_v2.model.Utente;
import com.sanitatrix.sanitatrix_v2.repository.MedicoRepository;
import com.sanitatrix.sanitatrix_v2.repository.PazienteRepository;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import com.sanitatrix.sanitatrix_v2.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired private UtenteService utenteService;
    @Autowired private UtenteRepository utenteRepository;
    @Autowired private MedicoRepository medicoRepository;
    @Autowired private PazienteRepository pazienteRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            Utente u = utenteService.registra(req);
            Map<String, Object> resp = new HashMap<>();
            resp.put("message", "Registrato OK");
            resp.put("id", u.getId());
            resp.put("ruolo", u.getRuolo().toString());
            if (u.getRuolo() != null) {
                if (u.getRuolo().toString().equals("MEDICO")) {
                    Optional<Medico> m = medicoRepository.findByUtenteId(u.getId());
                    m.ifPresent(medico -> resp.put("medicoId", medico.getId()));
                } else if (u.getRuolo().toString().equals("PAZIENTE")) {
                    Optional<Paziente> p = pazienteRepository.findByUtenteId(u.getId());
                    p.ifPresent(paziente -> resp.put("pazienteId", paziente.getId()));
                }
            }
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String identifier = payload.get("email");
        if (identifier == null) identifier = payload.get("username");
        if (identifier == null) identifier = payload.get("codiceFiscale");
        if (identifier == null) return ResponseEntity.status(401).body(Map.of("error", "Credenziali errate"));
        identifier = identifier.trim();
        Optional<Utente> opt = utenteRepository.findByEmail(identifier);
        if (opt.isEmpty()) {
            opt = utenteRepository.findByCodiceFiscale(identifier.toUpperCase());
        }
        String password = payload.get("password");
        if (opt.isEmpty() || password == null || !passwordEncoder.matches(password, opt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenziali errate"));
        }
        Utente u = opt.get();
        String token = Base64.getEncoder().encodeToString((u.getEmail() + ":" + System.currentTimeMillis()).getBytes());
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("email", u.getEmail());
        resp.put("codiceFiscale", u.getCodiceFiscale());
        resp.put("ruolo", u.getRuolo().toString());
        resp.put("utenteId", u.getId());
        if (u.getRuolo() != null) {
            String ruolo = u.getRuolo().toString();
            if (ruolo.equals("MEDICO")) {
                Optional<Medico> m = medicoRepository.findByUtenteId(u.getId());
                if (m.isPresent()) {
                    resp.put("medicoId", m.get().getId());
                    resp.put("nome", m.get().getNome());
                    resp.put("cognome", m.get().getCognome());
                    resp.put("specializzazione", m.get().getSpecializzazione() != null ? m.get().getSpecializzazione().toString() : null);
                }
            } else if (ruolo.equals("PAZIENTE")) {
                Optional<Paziente> p = pazienteRepository.findByUtenteId(u.getId());
                if (p.isPresent()) {
                    resp.put("pazienteId", p.get().getId());
                    resp.put("nome", p.get().getNome());
                    resp.put("cognome", p.get().getCognome());
                }
            }
        }
        return ResponseEntity.ok(resp);
    }
}