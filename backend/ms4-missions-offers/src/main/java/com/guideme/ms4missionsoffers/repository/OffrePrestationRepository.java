package com.guideme.ms4missionsoffers.repository;

import com.guideme.ms4missionsoffers.domain.OffrePrestation;
import com.guideme.ms4missionsoffers.domain.StatutOffre;
import com.guideme.ms4missionsoffers.domain.TypePrestation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OffrePrestationRepository extends JpaRepository<OffrePrestation, Long> {
    List<OffrePrestation> findByStatutAndLocalisationIgnoreCaseAndLangueIgnoreCaseAndTypePrestationAndDisponibleDuLessThanEqualAndDisponibleAuGreaterThanEqual(
            StatutOffre statut, String localisation, String langue, TypePrestation typePrestation,
            LocalDate debutMission, LocalDate finMission);
}
