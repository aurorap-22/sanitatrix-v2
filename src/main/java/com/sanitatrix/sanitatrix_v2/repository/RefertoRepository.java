package com.sanitatrix.sanitatrix_v2.repository;

import com.sanitatrix.sanitatrix_v2.model.Referto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface RefertoRepository extends JpaRepository<Referto, Long> {
    List<Referto> findByPaziente_Id (Long pazienteId);

    Optional<Referto> findByPrenotazione_Id (Long prenotazioneId);
}
