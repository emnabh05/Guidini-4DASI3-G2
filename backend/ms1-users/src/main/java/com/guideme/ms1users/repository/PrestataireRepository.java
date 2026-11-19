package com.guideme.ms1users.repository;

import com.guideme.ms1users.entity.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrestataireRepository extends JpaRepository<Prestataire, Long> {
    Optional<Prestataire> findByUtilisateurIdUtilisateur(Long idUtilisateur);
    boolean existsByUtilisateurIdUtilisateur(Long idUtilisateur);
}