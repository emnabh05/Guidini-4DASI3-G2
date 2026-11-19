package com.guideme.ms4missionsoffers.service;

import com.guideme.ms4missionsoffers.domain.Mission;
import com.guideme.ms4missionsoffers.domain.StatutMission;
import com.guideme.ms4missionsoffers.exception.RessourceIntrouvableException;
import com.guideme.ms4missionsoffers.repository.InvitationRepository;
import com.guideme.ms4missionsoffers.repository.MissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MissionService {
    private final MissionRepository repository;
    private final InvitationRepository invitationRepository;

    public MissionService(MissionRepository repository, InvitationRepository invitationRepository) {
        this.repository = repository;
        this.invitationRepository = invitationRepository;
    }

    public List<Mission> lister() {
        return repository.findAll();
    }

    public Mission rechercher(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Mission introuvable (id=" + id + ")"));
    }

    @Transactional
    public Mission creer(Mission saisie) {
        valider(saisie);
        if (saisie.getCreateurId() == null || saisie.getCreateurId() <= 0) {
            throw new IllegalArgumentException("Le créateur est obligatoire.");
        }
        Mission mission = new Mission(saisie.getCreateurId(), saisie.getTitre(), saisie.getDescription(),
                saisie.getLieu(), saisie.getDateDebut(), saisie.getDateFin());
        mission.definirBesoins(saisie);
        return repository.save(mission);
    }

    @Transactional
    public Mission modifier(Long id, Mission saisie) {
        valider(saisie);
        Mission entite = verrouiller(id);
        boolean modifiable = entite.getStatut() == StatutMission.BROUILLON
                || (entite.getStatut() == StatutMission.OUVERTE && !invitationRepository.existsByMissionId(id));
        if (!modifiable) {
            throw new IllegalStateException("La mission ne peut plus être modifiée après l’envoi des invitations.");
        }
        entite.modifier(saisie.getTitre(), saisie.getDescription(), saisie.getLieu(), saisie.getDateDebut(), saisie.getDateFin());
        entite.definirBesoins(saisie);
        return entite;
    }

    @Transactional
    public void supprimer(Long id) {
        Mission mission = verrouiller(id);
        exigerStatut(mission, StatutMission.BROUILLON, "Seul un brouillon peut être supprimé.");
        repository.delete(mission);
    }

    @Transactional
    public Mission publier(Long id) {
        Mission mission = verrouiller(id);
        exigerStatut(mission, StatutMission.BROUILLON, "Seul un brouillon peut être publié.");
        valider(mission);
        mission.changerStatut(StatutMission.OUVERTE);
        return mission;
    }

    @Transactional
    public Mission annuler(Long id) {
        Mission mission = verrouiller(id);
        if (mission.getStatut() == StatutMission.TERMINEE || mission.getStatut() == StatutMission.ANNULEE) {
            throw new IllegalStateException("Cette mission ne peut plus être annulée.");
        }
        mission.changerStatut(StatutMission.ANNULEE);
        return mission;
    }

    @Transactional
    public Mission terminer(Long id) {
        Mission mission = verrouiller(id);
        exigerStatut(mission, StatutMission.ATTRIBUEE, "La mission doit être attribuée pour être terminée.");
        mission.changerStatut(StatutMission.TERMINEE);
        return mission;
    }

    Mission verrouiller(Long id) {
        return repository.verrouiller(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Mission introuvable (id=" + id + ")"));
    }

    void exigerStatut(Mission mission, StatutMission statut, String message) {
        if (mission.getStatut() != statut) {
            throw new IllegalStateException(message);
        }
    }

    private void valider(Mission saisie) {
        if (saisie.getActiviteId() == null || saisie.getActiviteId() <= 0
                || saisie.getTypePrestation() == null
                || saisie.getNombreProfessionnels() == null || saisie.getNombreProfessionnels() <= 0
                || saisie.getLangue() == null || saisie.getLangue().isBlank()
                || saisie.getTitre() == null || saisie.getTitre().isBlank()
                || saisie.getDescription() == null || saisie.getDescription().isBlank()
                || saisie.getLieu() == null || saisie.getLieu().isBlank()) {
            throw new IllegalArgumentException("Renseigne l’activité, le titre, la description, le lieu, la langue, le type et le nombre de professionnels.");
        }
        if (saisie.getDateDebut() == null || saisie.getDateFin() == null
                || saisie.getDateFin().isBefore(saisie.getDateDebut())) {
            throw new IllegalArgumentException("Les dates de la mission sont invalides.");
        }
    }
}
