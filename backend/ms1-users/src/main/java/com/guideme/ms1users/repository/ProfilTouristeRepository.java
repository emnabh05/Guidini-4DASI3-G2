package com.guideme.ms1users.repository;

import com.guideme.ms1users.entity.ProfilTouriste;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfilTouristeRepository extends JpaRepository<ProfilTouriste, Long> {
    Optional<ProfilTouriste> findByUtilisateurIdUtilisateur(Long idUtilisateur);
    boolean existsByUtilisateurIdUtilisateur(Long idUtilisateur);
}