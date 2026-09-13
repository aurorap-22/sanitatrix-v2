package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.dto.RegisterRequest;
import com.sanitatrix.sanitatrix_v2.model.*;
import com.sanitatrix.sanitatrix_v2.repository.MedicoRepository;
import com.sanitatrix.sanitatrix_v2.repository.PazienteRepository;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UtenteService {

    @Autowired private UtenteRepository utenteRepository;
    @Autowired private PazienteRepository pazienteRepository;
    @Autowired private MedicoRepository medicoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<Utente> getAllUtenti() { return utenteRepository.findAll(); }
    public Utente saveUtente(Utente utente) { return utenteRepository.save(utente); }
    public Utente getUtenteById(Long id) { return utenteRepository.findById(id).orElse(null); }

    @Transactional
    public Utente registra(RegisterRequest req) {
        if (utenteRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email già usata");
        }
        Utente u = new Utente();
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setCodiceFiscale(req.getCodiceFiscale());
        u.setRuolo(req.getRuolo());
        u = utenteRepository.save(u);

        if (req.getRuolo() == Ruolo.PAZIENTE) {
            Paziente p = new Paziente();
            p.setNome(req.getNome());
            p.setCognome(req.getCognome());
            p.setDataNascita(req.getDataNascita());
            p.setTelefono(req.getTelefono());
            p.setIndirizzo(req.getIndirizzo());
            p.setUtente(u);
            pazienteRepository.save(p);
        } else {
            Medico m = new Medico();
            m.setNome(req.getNome());
            m.setCognome(req.getCognome());
            m.setTelefono(req.getTelefono());
            m.setSpecializzazione(req.getSpecializzazione());
            m.setUtente(u);
            medicoRepository.save(m);
        }
        return u;
    }
}