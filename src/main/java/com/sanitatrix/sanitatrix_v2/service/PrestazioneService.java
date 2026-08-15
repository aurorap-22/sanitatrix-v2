package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Prestazione;
import com.sanitatrix.sanitatrix_v2.repository.PrestazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PrestazioneService {

    @Autowired
    private PrestazioneRepository prestazioneRepository;

    public List<Prestazione> getAllPrestazioni(){
        return prestazioneRepository.findAll();
    }

    public Prestazione getPrestazioneById(Long id){
        return prestazioneRepository.findById(id).orElse(null);
    }

    public Prestazione savePrestazione(Prestazione prestazione){
        return prestazioneRepository.save(prestazione);
    }

    public void deletePrestazione(Long id){
        prestazioneRepository.deleteById(id);
    }


}
