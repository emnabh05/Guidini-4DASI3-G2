package com.guidini.ms2.repository;

import com.guidini.ms2.domain.EtapeCircuit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EtapeCircuitRepository extends JpaRepository<EtapeCircuit, Long> {

    List<EtapeCircuit> findByProduit_IdProduitOrderByNumeroJourAscOrdreAsc(Long idProduit);

    Optional<EtapeCircuit> findByIdEtapeAndProduit_IdProduit(Long idEtape, Long idProduit);
}
