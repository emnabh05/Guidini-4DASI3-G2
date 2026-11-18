package com.guideme.ms1users.repository;

import com.guideme.ms1users.entity.DocumentProfessionnel;
import com.guideme.ms1users.entity.StatutVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentProfessionnelRepository extends JpaRepository<DocumentProfessionnel, Long> {
    List<DocumentProfessionnel> findByUtilisateurIdUtilisateurOrderByDateDepotDesc(Long idUtilisateur);
    List<DocumentProfessionnel> findByStatutVerificationOrderByDateDepotAsc(StatutVerification statut);
}