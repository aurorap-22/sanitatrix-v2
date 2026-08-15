package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Prestazione;
import com.sanitatrix.sanitatrix_v2.repository.PrestazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestazione")
@CrossOrigin(origins = "http://localhost:5173")
public class PrestazioneController {

    @Autowired
    private PrestazioneRepository prestazioneRepository;

    @GetMapping
    public List<Prestazione> getAllPrestazioni(){
        return prestazioneRepository.findAll();
    }

    @PostMapping
    public Prestazione createPrestazione (@RequestBody Prestazione prestazione){
        return prestazioneRepository.save(prestazione);
    }

    @GetMapping("/{id}")
    public Prestazione getPrestazioneById(@PathVariable Long id){
        return prestazioneRepository.findById(id).orElse(null);
    }
}
