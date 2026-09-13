package com.sanitatrix.sanitatrix_v2.repository;

import com.sanitatrix.sanitatrix_v2.model.Medico;
import com.sanitatrix.sanitatrix_v2.model.TipoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


public interface MedicoRepository extends JpaRepository<Medico, Long> {

    List<Medico> findBySpecializzazione (TipoVisita specializzazione);
    List<Medico> findByCognomeContainingIgnoreCase(String cognome);
    Optional<Medico> findByUtenteId(Long idUtente);


}
