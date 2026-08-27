package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Paziente;
import com.sanitatrix.sanitatrix_v2.model.Ruolo;
import com.sanitatrix.sanitatrix_v2.model.Utente;
import com.sanitatrix.sanitatrix_v2.repository.PazienteRepository;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import com.sanitatrix.sanitatrix_v2.service.PazienteService;
import com.sanitatrix.sanitatrix_v2.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pazienti")
@CrossOrigin(origins = "http://localhost:5173")
public class PazienteController {

    @Autowired
    private PazienteService pazienteService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private UtenteRepository utenteRepository;


    @GetMapping
    public List<Paziente> getAll() {
        return pazienteService.getAllPazienti();
    }

    @PostMapping("/registrazione")
    public ResponseEntity<?> registrazione(@RequestBody Map<String, String> body){
        try{
            if(utenteRepository.findByEmail(body.get("email")).isPresent()) return ResponseEntity.badRequest().body("L'email che ha inserito è già in uso");
            if (body.get("codiceFiscale").length()!=16) return ResponseEntity.badRequest().body("Il codice fiscale ha 16 caratteri");
            Utente u = new Utente();
            u.setEmail(body.get("email"));
            u.setPassword(body.get("password"));
            u.setRuolo(Ruolo.PAZIENTE);
            Utente savedU = utenteService.saveUtente(u);
            Paziente p = new Paziente();
            p.setNome(body.get("nome"));
            p.setCognome(body.get("cognome"));
            p.setDataNascita(LocalDate.parse(body.get("dataNascita")));
            p.setTelefono(body.get("telefono"));
            p.setIndirizzo(body.get("indirizzo"));
            p.setUtente(savedU);
            return ResponseEntity.ok(pazienteService.savePaziente(p));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
