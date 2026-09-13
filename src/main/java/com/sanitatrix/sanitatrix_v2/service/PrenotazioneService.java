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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrenotazioneService {

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
        if (prenotazioneInput.getPrestazione() == null || prenotazioneInput.getPrestazione().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prestazione non inviata");
        }
        if (prenotazioneInput.getDataOra() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data/ora non inviata");
        }

        Long medicoId = prenotazioneInput.getMedico().getId();
        Long pazienteId = prenotazioneInput.getPaziente().getId();
        Long prestazioneId = prenotazioneInput.getPrestazione().getId();

        Medico medicoReale = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medico id " + medicoId + " non esiste"));
        Paziente pazienteReale = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paziente id " + pazienteId + " non esiste"));
        Prestazione prestazioneReale = prestazioneRepository.findById(prestazioneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prestazione id " + prestazioneId + " non esiste"));

        if (prenotazioneRepository.existsByMedicoIdAndDataOra(medicoId, prenotazioneInput.getDataOra())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot già occupato");
        }

        Prenotazione daSalvare = new Prenotazione();
        daSalvare.setMedico(medicoReale);
        daSalvare.setPaziente(pazienteReale);
        daSalvare.setPrestazione(prestazioneReale);
        daSalvare.setDataOra(prenotazioneInput.getDataOra());
        daSalvare.setDataFine(prenotazioneInput.getDataFine() != null ? prenotazioneInput.getDataFine() : prenotazioneInput.getDataOra().plusMinutes(30));
        daSalvare.setStato(prenotazioneInput.getStato() != null ? prenotazioneInput.getStato() : "PRENOTATA");

        return prenotazioneRepository.save(daSalvare);
    }

    // METODO CHE IL CONTROLLER CERCA ALLA RIGA 69 - deve tornare List<String> come da tuo controller
    public List<String> getSlotLiberi(Long medicoId, LocalDate data) {
        LocalDateTime inizio = data.atStartOfDay();
        LocalDateTime fine = data.atTime(23, 59, 59);
        List<Prenotazione> occupati = prenotazioneRepository.findByMedicoIdAndDataOraBetween(medicoId, inizio, fine);

        List<String> liberi = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        for (int h = 9; h < 18; h++) {
            for (int m = 0; m < 60; m += 30) {
                LocalDateTime slot = data.atTime(h, m);
                boolean isOccupato = occupati.stream().anyMatch(o -> o.getDataOra().equals(slot));
                if (!isOccupato) {
                    liberi.add(slot.format(fmt)); // ritorna "09:00", "09:30" ecc come vuole il tuo controller
                }
            }
        }
        return liberi;
    }
}