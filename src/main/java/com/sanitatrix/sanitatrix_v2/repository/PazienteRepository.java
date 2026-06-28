package com.sanitatrix.sanitatrix_v2.repository;

import com.sanitatrix.sanitatrix_v2.model.Paziente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PazienteRepository extends JpaRepository<Paziente, Long> {

    Optional<Paziente> findByEmail(String email);
    Optional<Paziente> findByCodiceFiscale(String codiceFiscale);
    List<Paziente> findByCognomeContainigIgnoreCase(String cognome);
}
