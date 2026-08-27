package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Utente;
import com.sanitatrix.sanitatrix_v2.repository.MedicoRepository;
import com.sanitatrix.sanitatrix_v2.repository.PazienteRepository;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> b){
        String email= b.get("email");
        String password= b.get("password");
        Utente u= utenteRepository.findByEmail(b.get("email")).orElseThrow(() -> new RuntimeException("UTENTE NON TROVATO"));
        if(!u.getPassword().equals(b.get("password"))) throw new RuntimeException("PASSWORD ERRATA");
        Map<String,Object> r = new HashMap<>();
        r.put("id", u.getId());
        r.put("email", u.getEmail());
        r.put("ruolo", u.getRuolo().name());
        pazienteRepository.findByUtenteId(u.getId()).ifPresent(p -> r.put("pazienteId", p.getId()));
        medicoRepository.findByUtenteId(u.getId()).ifPresent(m -> r.put("medicoId", m.getId()));
        return r;

    }
}
