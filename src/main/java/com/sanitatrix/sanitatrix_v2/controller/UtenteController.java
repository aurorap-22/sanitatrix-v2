package com.sanitatrix.sanitatrix_v2.controller;
import com.sanitatrix.sanitatrix_v2.model.Utente;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import com.sanitatrix.sanitatrix_v2.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utenti")
@CrossOrigin(origins = "http://localhost:5173")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping
    public List<Utente> getAllUtenti(){
        return utenteService.getAllUtenti();
    }

    @PostMapping
    public Utente addUser(@RequestBody Utente utente){
        return utenteService.saveUtente(utente);
    }

    @GetMapping("/{id}")
    public Utente getUtenteById(@PathVariable Long id){
        return utenteService.getUtenteById(id);
    }
}
