package com.guideme.ms1users.repository;

import com.guideme.ms1users.entity.ProfilProfessionnel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfilProfessionnelRepository extends JpaRepository<ProfilProfessionnel, Long> {
    Optional<ProfilProfessionnel> findByUtilisateurIdUtilisateur(Long idUtilisateur);
    boolean existsByUtilisateurIdUtilisateur(Long idUtilisateur);
}