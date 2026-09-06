package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Prenotazione;
import com.sanitatrix.sanitatrix_v2.repository.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    private static final LocalTime ORARIO_APERTURA = LocalTime.of(9,0);
    private static final LocalTime ORARIO_CHIUSURA = LocalTime.of(19, 30);
    private static final int DURATA_VISITA_MINUTI = 30;

    public Prenotazione createPrenotazione(Prenotazione prenotazione){
       if(prenotazione.getDataOra() != null){
           prenotazione.setDataFine(prenotazione.getDataOra().plusMinutes(30));
       }
       prenotazione.setStato("PRENOTATA");
       prenotazione.setPrestazione(null);
       return prenotazioneRepository.save(prenotazione);
    }

    public List<String> getSlotDisponibili(Long medicoId, LocalDate data){
        List<String> tuttiGliSlot = new ArrayList<>();
        LocalTime ora = ORARIO_APERTURA;
        while (!ora.isAfter(LocalTime.of(19,0))){
            tuttiGliSlot.add(ora.toString().substring(0,5));
            ora = ora.plusMinutes(DURATA_VISITA_MINUTI);
        }

        LocalDateTime inizioGiornata = data.atStartOfDay();
        LocalDateTime fineGiornata = data.atTime(23, 59, 59);
        List<Prenotazione> delGiorno = prenotazioneRepository.findByMedicoIdAndDataOraBetween(medicoId, inizioGiornata, fineGiornata);

        List<String> occupati = delGiorno.stream()
                .map(p -> p.getDataOra().toLocalTime().toString().substring(0,5))
                .toList();

        tuttiGliSlot.removeAll(occupati);
        return tuttiGliSlot;
    }

    public List<Prenotazione> findByMedicoId(Long id){ return prenotazioneRepository.findByMedicoId(id); }
    public Prenotazione savePrenotazione(Prenotazione prenotazione){ return prenotazioneRepository.save(prenotazione); }
    public List<Prenotazione> findAll(){ return prenotazioneRepository.findAll(); }
    public List<Prenotazione> findByPazienteId(Long pazienteId){ return prenotazioneRepository.findByPazienteId(pazienteId); }
    public List<Prenotazione> findByStato(String stato){ return prenotazioneRepository.findByStato(stato); }
    public List<Prenotazione> findByMedicoIdAndDataOraBetween(Long medicoId, LocalDateTime inizio, LocalDateTime fine){
        return prenotazioneRepository.findByMedicoIdAndDataOraBetween(medicoId, inizio, fine);
    }
}