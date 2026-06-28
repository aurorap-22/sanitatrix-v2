package com.sanitatrix.sanitatrix_v2.repository;

import com.sanitatrix.sanitatrix_v2.model.Medico;
import com.sanitatrix.sanitatrix_v2.model.TipoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByEmail(String email);
    Optional<Medico> findByCodiceFiscale(String codiceFiscale);
    List<Medico> findBySpecializzazione (TipoVisita specializzazione);
    List<Medico> findByCognomeContainingIgnoreCase(String cognome);


}
