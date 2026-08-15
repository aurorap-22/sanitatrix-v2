package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Medico;
import com.sanitatrix.sanitatrix_v2.repository.MedicoRepository;
import com.sanitatrix.sanitatrix_v2.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    public List<Medico> getAllMedici(){
        return medicoRepository.findAll();
    }

    public Medico saveMedico(Medico medico){
        return medicoRepository.save(medico);
    }
    public Medico getMedicoById(Long id){
        return medicoRepository.findById(id).orElse(null);
    }
}
