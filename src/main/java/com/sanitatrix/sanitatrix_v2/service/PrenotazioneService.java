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
    private static final int DURATA_VISITA_MINUTI= 30;

    //CREA PRENOTAZIONE
    public Prenotazione createPrenotazione(Prenotazione prenotazione){
        LocalDateTime dataOra = prenotazione.getDataOra();
        LocalDateTime dataFine = dataOra.plusMinutes(DURATA_VISITA_MINUTI);
        prenotazione.setDataFine(dataFine);

        //CONTROLLO ORARIO
        LocalTime oraInizio = dataOra.toLocalTime();
        if (oraInizio.isBefore(ORARIO_APERTURA) || dataFine.toLocalTime().isAfter(ORARIO_CHIUSURA)){
            throw new RuntimeException("Orario non valido, è possibile effettuare le visite dalle 9:00, con ultimaa slot disponibile dalle 19:00 alle 19:30");
        }

        //CONTROLLO SLOT MEZZ'ORA
        if (oraInizio.getMinute() != 0 && oraInizio.getMinute() != 30){
            throw new RuntimeException("Le prenotazioni sono disponibili solo alle :00 o alle :30");
        }

        // CONTROLLO SETTIMANA
        DayOfWeek giorno = dataOra.getDayOfWeek();
        if(giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY){
            throw new RuntimeException("E' possibile prenotare le visite solo dal lunedì al venerdì");
        }

        //CONTROLLO SOVRAPPOSIZIONE
        LocalDateTime inizioControllo = dataOra.minusMinutes(DURATA_VISITA_MINUTI);
        List<Prenotazione> sovrapposte = prenotazioneRepository.findByMedicoIdAndDataOraBetween(prenotazione.getMedico().getId(), inizioControllo, dataFine);

        if(!sovrapposte.isEmpty()){
            throw new RuntimeException("il dottore non e' disponibile in questa fascia oraria");
        }

        prenotazione.setStato("PRENOTATA");
        return prenotazioneRepository.save(prenotazione);


    }

    //MENU A TENDINA
    public List<String> getSlotDisponibili(Long medicoId, LocalDate data){
        List<String> tuttiGliSlot = new ArrayList<>();
        List<String> slotOccupati = new ArrayList<>();

        //GENERAZIONE SLOT DI MEZZ'ORA DA ORARIO APERTURA A CHIUSURA
        LocalTime ora = ORARIO_APERTURA;
        while (!ora.isAfter(LocalTime.of(19,0))){
            tuttiGliSlot.add(ora.toString());
            ora = ora.plusMinutes(DURATA_VISITA_MINUTI);
        }
        //prenotazioni già fatte x giorno
        LocalDateTime inizioGiornata = data.atStartOfDay();
        LocalDateTime fineGiornata = data.atTime(23, 59, 59);
        List<Prenotazione> prenotazioniDelGiorno = prenotazioneRepository.findByMedicoIdAndDataOraBetween(medicoId,inizioGiornata, fineGiornata);

        //ore occupate
        for (Prenotazione p : prenotazioniDelGiorno){
            slotOccupati.add(p.getDataOra().toLocalTime().toString());
        }
        //slot liberi
        tuttiGliSlot.removeAll(slotOccupati);
        return tuttiGliSlot;
    }


    public Prenotazione savePrenotazione(Prenotazione prenotazione){
        return prenotazioneRepository.save(prenotazione);
    }

    public List<Prenotazione> findAll(){
        return prenotazioneRepository.findAll();
    }

    public List<Prenotazione> findByPazienteId(Long pazienteId){
        return prenotazioneRepository.findByPazienteId(pazienteId);
    }

    public List<Prenotazione> findByMedicoIdAndDataOraBetween(Long medicoId, LocalDateTime inizio, LocalDateTime fine){
        return prenotazioneRepository.findByMedicoIdAndDataOraBetween(medicoId, inizio, fine);
    }

    public List<Prenotazione> findByStato(String stato){
        return prenotazioneRepository.findByStato(stato);
    }
}
