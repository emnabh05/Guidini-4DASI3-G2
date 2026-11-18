package com.guideme.ms1users.repository;

import com.guideme.ms1users.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdUtilisateurNot(String email, Long idUtilisateur);
}