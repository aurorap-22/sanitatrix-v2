package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.dto.RegisterRequest;
import com.sanitatrix.sanitatrix_v2.model.Utente;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import com.sanitatrix.sanitatrix_v2.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired private UtenteService utenteService;
    @Autowired private UtenteRepository utenteRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {

            Utente u = utenteService.registra(req);
            return ResponseEntity.ok(Map.of("message", "Registrato OK", "id", u.getId()));
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
        if (identifier == null) return ResponseEntity.status(401).body("Credenziali errate");
        identifier = identifier.trim();
        var opt = utenteRepository.findByEmail(identifier);
        if (opt.isEmpty()) {
            opt = utenteRepository.findByCodiceFiscale(identifier.toUpperCase());
        }
        String password = payload.get("password");
        if (opt.isEmpty() || !passwordEncoder.matches(password, opt.get().getPassword())) {
            return ResponseEntity.status(401).body("Credenziali errate");
        }
        Utente u = opt.get();
        Map<String, Object> resp = new HashMap<>();
        resp.put("email", u.getEmail());
        resp.put("codiceFiscale", u.getCodiceFiscale());
        resp.put("ruolo", u.getRuolo().toString());
        resp.put("id", u.getId());
        return ResponseEntity.ok(resp);
    }
}