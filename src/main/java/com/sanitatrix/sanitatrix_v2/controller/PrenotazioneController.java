package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Prenotazione;
import com.sanitatrix.sanitatrix_v2.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/prenotazioni")
@CrossOrigin(origins = "http://localhost:5173")
public class PrenotazioneController {
    @Autowired
    private PrenotazioneService prenotazioneService;
    //POST testaddprenot
    @PostMapping("/add-prenotazione")
    public ResponseEntity<Prenotazione> addPrenotazione(@RequestBody Prenotazione prenotazione){
        Prenotazione saved = prenotazioneService.createPrenotazione(prenotazione);
        return ResponseEntity.ok(saved);
    }
    //GET test-getprenotazioni
    @GetMapping("/get-prenotazioni")
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioni() {
        return ResponseEntity.ok(prenotazioneService.findAll());
    }
    //GET test-prenotazioni-paziente-id
    @GetMapping("/prenotazioni/paziente/{id}")
    public ResponseEntity<List<Prenotazione>> getByPaziente(@PathVariable Long id){
        return ResponseEntity.ok(prenotazioneService.findByPazienteId(id));
    }

    @GetMapping("/medico/{id}/per-data")
    public ResponseEntity<List<Prenotazione>> getByMedicoEData(@PathVariable Long id, @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime inizio,
                                                               @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime fine){
        return ResponseEntity.ok(prenotazioneService.findByMedicoIdAndDataOraBetween(id, inizio, fine));
    }
    @GetMapping("/prenotazioni/stato/{stato}")
    public ResponseEntity<List<Prenotazione>> getByStato(@PathVariable String stato) {
        return ResponseEntity.ok(prenotazioneService.findByStato(stato));
    }

    @GetMapping("/medico/{id}")
    public ResponseEntity<List<Prenotazione>> getByMedico(@PathVariable Long id){
        return ResponseEntity.ok(prenotazioneService.findByMedicoId(id));
    }



}
