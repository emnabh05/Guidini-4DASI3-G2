package com.guideme.ms4missionsoffers.repository;

import com.guideme.ms4missionsoffers.domain.Invitation;
import com.guideme.ms4missionsoffers.domain.StatutInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    @Query("select i.mission.id from Invitation i where i.id = :id")
    Optional<Long> rechercherMissionId(@Param("id") Long id);

    boolean existsByMissionId(Long missionId);

    boolean existsByMissionIdAndPrestataireId(Long missionId, Long prestataireId);

    long countByMissionIdAndStatut(Long missionId, StatutInvitation statut);

    List<Invitation> findByMissionIdOrderById(Long missionId);

    List<Invitation> findByPrestataireIdOrderByIdDesc(Long prestataireId);
}
