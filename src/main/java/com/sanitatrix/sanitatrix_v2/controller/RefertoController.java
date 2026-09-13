package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Paziente;
import com.sanitatrix.sanitatrix_v2.model.Prenotazione;
import com.sanitatrix.sanitatrix_v2.model.Referto;
import com.sanitatrix.sanitatrix_v2.service.RefertoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/referti")
@CrossOrigin(origins = "*")
public class RefertoController {

    @Autowired
    private RefertoService refertoService;

    @PostMapping
    public ResponseEntity<?> createReferto(@RequestBody Map<String, Object> body) {
        try {
            Referto referto = new Referto();

            Long pazienteId = Long.valueOf(body.get("pazienteId").toString());
            Paziente p = new Paziente();
            p.setId(pazienteId);
            referto.setPaziente(p);

            Long prenId = Long.valueOf(body.get("prenotazioneId").toString());
            Prenotazione pre = new Prenotazione();
            pre.setId(prenId);
            referto.setPrenotazione(pre);

            referto.setDiagnosi((String) body.get("descrizione"));
            referto.setTerapia((String) body.get("terapia"));
            if (body.get("esamiConsigliati") != null) {
                referto.setEsamiConsigliati((String) body.get("esamiConsigliati"));
            }

            Referto salvato = refertoService.saveReferto(referto);
            return ResponseEntity.ok(salvato);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Referto> getAll() { return refertoService.getAllReferti(); }

    @GetMapping("/paziente/{id}")
    public List<Referto> getByPaziente(@PathVariable Long id) {
        return refertoService.getByPazienteId(id);
    }
}