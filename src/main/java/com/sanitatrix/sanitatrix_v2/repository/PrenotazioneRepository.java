package com.sanitatrix.sanitatrix_v2.repository;

import com.sanitatrix.sanitatrix_v2.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByPazienteId (Long pazienteId);
    List<Prenotazione> findByMedicoIdAndData(Long medicoId, LocalDate data);
    List<Prenotazione> findByStato (String stato);

}
