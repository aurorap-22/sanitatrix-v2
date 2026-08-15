package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Paziente;
import com.sanitatrix.sanitatrix_v2.model.Utente;
import com.sanitatrix.sanitatrix_v2.repository.PazienteRepository;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import com.sanitatrix.sanitatrix_v2.service.PazienteService;
import com.sanitatrix.sanitatrix_v2.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pazienti")
public class PazienteController {

    @Autowired
    private PazienteService pazienteService;

    @Autowired
    private UtenteService utenteService;

    @GetMapping
    public List<Paziente> getAllPazienti() {
        return pazienteService.getAllPazienti();
    }

    @PostMapping
    public ResponseEntity<?> addPaziente(@RequestBody Paziente paziente) {
        try {
            Long idUtente = paziente.getUtente().getId();
            Utente utente = utenteService.getUtenteById(idUtente);
            if (utente == null) return ResponseEntity.badRequest().body("utente non trovato");
            paziente.setUtente(utente);
            Paziente saved = pazienteService.savePaziente(paziente);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
