package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Medico;
import com.sanitatrix.sanitatrix_v2.model.Utente;
import com.sanitatrix.sanitatrix_v2.repository.MedicoRepository;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import com.sanitatrix.sanitatrix_v2.service.MedicoService;
import com.sanitatrix.sanitatrix_v2.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medici")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private UtenteService utenteService;

    @GetMapping
    public List<Medico> getAllMedici(){
        return medicoService.getAllMedici();
    }

    @PostMapping
    public ResponseEntity<?> addMedico(@RequestBody Medico medico){
        try {
            Long idUtente = medico.getUtente().getId();
            Utente utente = utenteService.getUtenteById(idUtente);
            if(utente==null) return ResponseEntity.badRequest().body("Utente non trovato");
            medico.setUtente(utente);
            Medico saved= medicoService.saveMedico(medico);
            return ResponseEntity.ok(saved);


        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Medico getMedicoById(@PathVariable Long id){
        return medicoService.getMedicoById(id);
    }
}
