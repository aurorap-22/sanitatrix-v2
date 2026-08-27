package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Disponibilita;
import com.sanitatrix.sanitatrix_v2.service.DisponibilitaService;
import com.sanitatrix.sanitatrix_v2.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilita")
@CrossOrigin(origins = "http://localhost:5173")
public class DisponibilitaController {

    @Autowired
    private PrenotazioneService prenotazioneService;



    @GetMapping("/{medicoId}/{data}")
    public ResponseEntity<?> getSlot(@PathVariable Long medicoId, @PathVariable String data){
        LocalDate dataParsed = LocalDate.parse(data);
        return ResponseEntity.ok(prenotazioneService.getSlotDisponibili(medicoId, dataParsed));
    }

}
