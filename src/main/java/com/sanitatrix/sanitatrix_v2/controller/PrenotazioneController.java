package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Prenotazione;
import com.sanitatrix.sanitatrix_v2.repository.PrenotazioneRepository;
import com.sanitatrix.sanitatrix_v2.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
@CrossOrigin(origins = "http://localhost:5173")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @GetMapping("/get-prenotazioni")
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioni(){
        return ResponseEntity.ok(prenotazioneService.findAll());
    }

    @PostMapping("/add-prenotazione")
    public ResponseEntity<Prenotazione> addPrenotazione(@RequestBody Prenotazione prenotazione){
        return ResponseEntity.ok(prenotazioneService.createPrenotazione(prenotazione));
    }

    @GetMapping
    public ResponseEntity<List<Prenotazione>> getAll(){
        return ResponseEntity.ok(prenotazioneService.findAll());
    }

    @GetMapping("/mie")
    public ResponseEntity<List<Prenotazione>> getMie(){
        return ResponseEntity.ok(prenotazioneService.findAll());
    }

    // --- QUESTO È QUELLO CHE TI MANCAVA ---
    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<Prenotazione>> getByMedico(@PathVariable Long idMedico){
        List<Prenotazione> lista = prenotazioneRepository.findByMedicoId(idMedico);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/paziente/{idPaziente}")
    public ResponseEntity<List<Prenotazione>> getByPaziente(@PathVariable Long idPaziente){
        List<Prenotazione> lista = prenotazioneRepository.findByPazienteId(idPaziente);
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<?> crea(@RequestBody Prenotazione prenotazione){
        try{
            return ResponseEntity.ok(prenotazioneService.createPrenotazione(prenotazione));
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/slot")
    public ResponseEntity<List<String>> getSlot(
            @RequestParam Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data){
        if(data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY){
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(List.of("09:00","09:30","10:00","10:30","11:00","11:30","15:00","15:30","16:00"));
    }
}