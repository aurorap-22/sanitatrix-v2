package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Disponibilita;
import com.sanitatrix.sanitatrix_v2.service.DisponibilitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilita")
public class DisponibilitaController {

    @Autowired
    private DisponibilitaService disponibilitaService;

    @GetMapping
    public List<Disponibilita> getAllDisponibilita() {
        return disponibilitaService.getAllDisponibilita();
    }

    @GetMapping("/medico/ {medicoId}")
    public List<Disponibilita> getByMedico(@PathVariable Long medicoId){
        return disponibilitaService.getByMedicoId(medicoId);
    }

    @PostMapping
    public Disponibilita createDisponibilita(@RequestBody Disponibilita disponibilita){
        return disponibilitaService.saveDisponibilita(disponibilita);
    }

    @DeleteMapping("/{id}")
    public void deleteDisponibilita(@PathVariable Long id){
        disponibilitaService.deleteDisponibilita(id);
    }

}
