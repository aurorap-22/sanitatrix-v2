package com.sanitatrix.sanitatrix_v2.repository;

import com.sanitatrix.sanitatrix_v2.model.Disponibilita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibilitaRepository extends JpaRepository<Disponibilita, Long> {
    List<Disponibilita> findByMedicoIdAndData(Long medicoId, LocalDate data);
}
