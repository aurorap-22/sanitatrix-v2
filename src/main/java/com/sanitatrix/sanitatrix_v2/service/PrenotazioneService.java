package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Medico;
import com.sanitatrix.sanitatrix_v2.model.Paziente;
import com.sanitatrix.sanitatrix_v2.model.Prenotazione;
import com.sanitatrix.sanitatrix_v2.model.Prestazione;
import com.sanitatrix.sanitatrix_v2.repository.MedicoRepository;
import com.sanitatrix.sanitatrix_v2.repository.PazienteRepository;
import com.sanitatrix.sanitatrix_v2.repository.PrenotazioneRepository;
import com.sanitatrix.sanitatrix_v2.repository.PrestazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrenotazioneService {

    private static final LocalTime ORARIO_INIZIO = LocalTime.of(9, 0);
    private static final LocalTime ORARIO_FINE = LocalTime.of(19, 0);

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PazienteRepository pazienteRepository;
    @Autowired
    private PrestazioneRepository prestazioneRepository;

    public List<Prenotazione> findAll() {
        return prenotazioneRepository.findAll();
    }

    // METODO CHE IL CONTROLLER CERCA ALLA RIGA 31 e 59
    public Prenotazione createPrenotazione(Prenotazione prenotazioneInput) {

        if (prenotazioneInput.getMedico() == null || prenotazioneInput.getMedico().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medico non inviato");
        }
        if (prenotazioneInput.getPaziente() == null || prenotazioneInput.getPaziente().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paziente non inviato");
        }
        if (prenotazioneInput.getDataOra() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data/ora non inviata");
        }

        Long medicoId = prenotazioneInput.getMedico().getId();
        Long pazienteId = prenotazioneInput.getPaziente().getId();

        Medico medicoReale = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medico id " + medicoId + " non esiste"));
        Paziente pazienteReale = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paziente id " + pazienteId + " non esiste"));

        Prestazione prestazioneReale;
        if (prenotazioneInput.getPrestazione() != null && prenotazioneInput.getPrestazione().getId() != null) {
            Long prestazioneId = prenotazioneInput.getPrestazione().getId();
            prestazioneReale = prestazioneRepository.findById(prestazioneId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prestazione id " + prestazioneId + " non esiste"));
        } else {
            prestazioneReale = prestazioneRepository.findFirstByTipoVisita(medicoReale.getSpecializzazione())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Nessuna prestazione trovata per la specializzazione " + medicoReale.getSpecializzazione()));
        }

        LocalDateTime dataOra = prenotazioneInput.getDataOra();
        DayOfWeek giorno = dataOra.getDayOfWeek();
        if (giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le prenotazioni sono consentite solo dal lunedì al venerdì");
        }

        LocalTime orario = dataOra.toLocalTime();
        if (orario.isBefore(ORARIO_INIZIO) || orario.isAfter(ORARIO_FINE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "L'orario di prenotazione deve essere compreso tra le " + ORARIO_INIZIO + " e le " + ORARIO_FINE);
        }

        if (prenotazioneRepository.existsByMedicoIdAndDataOra(medicoId, dataOra)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot già occupato");
        }

        Prenotazione daSalvare = new Prenotazione();
        daSalvare.setMedico(medicoReale);
        daSalvare.setPaziente(pazienteReale);
        daSalvare.setPrestazione(prestazioneReale);
        daSalvare.setDataOra(dataOra);
        daSalvare.setDataFine(prenotazioneInput.getDataFine() != null ? prenotazioneInput.getDataFine() : dataOra.plusMinutes(30));
        daSalvare.setStato(prenotazioneInput.getStato() != null ? prenotazioneInput.getStato() : "PRENOTATA");

        return prenotazioneRepository.save(daSalvare);
    }

    // METODO CHE IL CONTROLLER CERCA ALLA RIGA 69 - deve tornare List<String> come da tuo controller
    public List<String> getSlotLiberi(Long medicoId, LocalDate data) {
        DayOfWeek giorno = data.getDayOfWeek();
        if (giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY) {
            return new ArrayList<>();
        }

        LocalDateTime inizio = data.atTime(ORARIO_INIZIO);
        LocalDateTime fine = data.atTime(ORARIO_FINE);
        List<Prenotazione> occupati = prenotazioneRepository.findByMedicoIdAndDataOraBetween(medicoId, inizio, fine);

        List<String> liberi = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        for (int h = ORARIO_INIZIO.getHour(); h < ORARIO_FINE.getHour(); h++) {
            for (int m = 0; m < 60; m += 30) {
                LocalDateTime slot = data.atTime(h, m);
                boolean isOccupato = occupati.stream().anyMatch(o -> o.getDataOra().equals(slot));
                if (!isOccupato) {
                    liberi.add(slot.format(fmt));
                }
            }
        }
        LocalDateTime slotFine = data.atTime(ORARIO_FINE);
        boolean slotFineOccupato = occupati.stream().anyMatch(o -> o.getDataOra().equals(slotFine));
        if (!slotFineOccupato) {
            liberi.add(slotFine.format(fmt));
        }
        return liberi;
    }
}