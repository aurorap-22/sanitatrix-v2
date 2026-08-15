package com.sanitatrix.sanitatrix_v2.repository;

import com.sanitatrix.sanitatrix_v2.model.Paziente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

public interface PazienteRepository extends JpaRepository<Paziente, Long> {

    Optional<Paziente> findByUtenteId(Long idUtente);
}
