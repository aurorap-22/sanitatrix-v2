package com.sanitatrix.sanitatrix_v2.repository;

import com.sanitatrix.sanitatrix_v2.model.Disponibilita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DisponibilitaRepository extends JpaRepository<Disponibilita, Long> {
    List<Disponibilita> findByMedico_Id(Long medicoId);
}
