package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Utente;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public List<Utente> getAllUtenti(){
        return utenteRepository.findAll();
    }

    public Utente saveUtente(Utente utente){
        return utenteRepository.save(utente);
    }

    public Utente getUtenteById(Long id){
        return utenteRepository.findById(id).orElse(null);
    }

}
