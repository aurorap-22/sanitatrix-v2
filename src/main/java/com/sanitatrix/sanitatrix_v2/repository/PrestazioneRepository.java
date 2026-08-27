package com.sanitatrix.sanitatrix_v2.repository;

import com.sanitatrix.sanitatrix_v2.model.Prestazione;
import com.sanitatrix.sanitatrix_v2.model.TipoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PrestazioneRepository extends JpaRepository<Prestazione, Long> {
    List<Prestazione> findByTipoVisita(TipoVisita tipoVisita);

}
