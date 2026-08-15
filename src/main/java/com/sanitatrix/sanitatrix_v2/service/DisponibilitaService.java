package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Disponibilita;
import com.sanitatrix.sanitatrix_v2.repository.DisponibilitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilitaService {

    @Autowired
    private DisponibilitaRepository disponibilitaRepository;

    public List<Disponibilita> getAllDisponibilita(){
        return disponibilitaRepository.findAll();
    }

    public List<Disponibilita> getByMedicoId( Long medicoId){
        return disponibilitaRepository.findByMedico_Id(medicoId);
    }

    public Disponibilita saveDisponibilita(Disponibilita disponibilita){
        return disponibilitaRepository.save(disponibilita);
    }

    public void deleteDisponibilita(Long id){
        disponibilitaRepository.deleteById(id);
    }

}
