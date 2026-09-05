package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Paziente;
import com.sanitatrix.sanitatrix_v2.model.Prenotazione;
import com.sanitatrix.sanitatrix_v2.model.Referto;
import com.sanitatrix.sanitatrix_v2.repository.PazienteRepository;
import com.sanitatrix.sanitatrix_v2.repository.PrenotazioneRepository;
import com.sanitatrix.sanitatrix_v2.repository.RefertoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RefertoService {

    @Autowired
    private RefertoRepository refertoRepository;

    @Autowired
    private PazienteRepository pazienteRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    public List<Referto> getAllReferti(){
        return refertoRepository.findAll();
    }

    public List<Referto> getByPazienteId(Long pazienteId){
        return refertoRepository.findByPaziente_Id(pazienteId);
    }

    public Referto saveReferto (Referto referto){

        if(referto.getPrenotazione() != null && refertoRepository.existsByPrenotazione_Id(referto.getPrenotazione().getId())){
            throw new RuntimeException("Referto esistente per questa prenotazione");
        }
        Paziente paz= pazienteRepository.findById(referto.getPaziente().getId()).orElseThrow();
        Prenotazione pre = prenotazioneRepository.findById(referto.getPrenotazione().getId()).orElseThrow();

        referto.setPaziente(paz);
        referto.setPrenotazione(pre);
        if(referto.getDataReferto()==null) referto.setDataReferto(LocalDate.now());

        return refertoRepository.save(referto);

    }
}
