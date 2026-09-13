package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Paziente;
import com.sanitatrix.sanitatrix_v2.repository.PazienteRepository;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PazienteService {

    @Autowired
    private PazienteRepository pazienteRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    public List<Paziente> getAllPazienti(){
        return pazienteRepository.findAll();
    }

    public Paziente savePaziente(Paziente paziente){
        return pazienteRepository.save(paziente);
    }

    public Paziente getPazienteById(Long id){
        return pazienteRepository.findById(id).orElse(null);
    }
}

