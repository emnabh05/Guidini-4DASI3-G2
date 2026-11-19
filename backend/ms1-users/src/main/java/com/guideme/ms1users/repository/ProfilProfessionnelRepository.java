package com.guideme.ms1users.repository;

import com.guideme.ms1users.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfilProfessionnelRepository extends JpaRepository<ProfilProfessionnel, Long> {
    Optional<ProfilProfessionnel> findByUtilisateurIdUtilisateur(Long idUtilisateur);
    boolean existsByUtilisateurIdUtilisateur(Long idUtilisateur);

    @Query("""
        SELECT p FROM ProfilProfessionnel p
        WHERE p.utilisateur.statut = :statut
          AND p.disponibilite = :dispo
          AND (:type IS NULL OR p.typeProfessionnel = :type)
          AND (:langue IS NULL OR LOWER(p.langues) LIKE LOWER(CONCAT('%', :langue, '%')))
        """)
    List<ProfilProfessionnel> rechercher(@Param("statut") StatutUtilisateur statut,
                                         @Param("dispo") Disponibilite dispo,
                                         @Param("type") TypeProfessionnel type,
                                         @Param("langue") String langue);
}