package com.guidini.ms2.repository;

import com.guidini.ms2.domain.CategorieProduit;
import com.guidini.ms2.domain.Produit;
import com.guidini.ms2.domain.StatutPublication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findByStatut(StatutPublication statut);

    Optional<Produit> findByIdProduitAndStatut(Long idProduit, StatutPublication statut);

    List<Produit> findByProprietaireId(Long proprietaireId);

    List<Produit> findByStatutAndCategorie(StatutPublication statut, CategorieProduit categorie);

    List<Produit> findByStatutAndDestinationContainingIgnoreCase(
            StatutPublication statut, String destination);
}
