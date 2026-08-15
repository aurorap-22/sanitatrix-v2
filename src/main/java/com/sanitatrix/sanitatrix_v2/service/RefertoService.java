package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Referto;
import com.sanitatrix.sanitatrix_v2.repository.RefertoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefertoService {

    @Autowired
    private RefertoRepository refertoRepository;

    public List<Referto> getAllReferti(){
        return refertoRepository.findAll();
    }

    public List<Referto> getByPazienteId(Long pazienteId){
        return refertoRepository.findByPaziente_Id(pazienteId);
    }

    public Referto saveReferto (Referto referto){
        return refertoRepository.save(referto);
    }
}
