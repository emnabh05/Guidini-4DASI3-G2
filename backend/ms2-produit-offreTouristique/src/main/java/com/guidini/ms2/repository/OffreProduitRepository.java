package com.guidini.ms2.repository;

import com.guidini.ms2.domain.OffreProduit;
import com.guidini.ms2.domain.StatutPublication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OffreProduitRepository extends JpaRepository<OffreProduit, Long> {

    List<OffreProduit> findByStatut(StatutPublication statut);

    Optional<OffreProduit> findByIdOffreAndStatut(Long idOffre, StatutPublication statut);

    List<OffreProduit> findByProprietaireId(Long proprietaireId);

    List<OffreProduit> findByProduits_IdProduit(Long idProduit);
}
