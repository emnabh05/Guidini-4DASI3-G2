package com.guideme.ms4missionsoffers.service;

import com.guideme.ms4missionsoffers.domain.Invitation;
import com.guideme.ms4missionsoffers.domain.Mission;
import com.guideme.ms4missionsoffers.domain.OffrePrestation;
import com.guideme.ms4missionsoffers.domain.StatutInvitation;
import com.guideme.ms4missionsoffers.domain.StatutMission;
import com.guideme.ms4missionsoffers.exception.RessourceIntrouvableException;
import com.guideme.ms4missionsoffers.repository.InvitationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class InvitationService {
    private final InvitationRepository repository;
    private final MissionService missionService;
    private final OffrePrestationService offreService;

    public InvitationService(InvitationRepository repository, MissionService missionService,
                             OffrePrestationService offreService) {
        this.repository = repository;
        this.missionService = missionService;
        this.offreService = offreService;
    }

    public List<Invitation> lister() {
        return repository.findAll();
    }

    public Invitation rechercher(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Invitation introuvable (id=" + id + ")"));
    }

    @Transactional
    public Invitation creer(Long missionId, Invitation saisie) {
        Mission mission = missionOuverte(missionId);
        Long professionnel = saisie.getPrestataireId();
        if (professionnel == null || professionnel <= 0) {
            throw new IllegalArgumentException("Le professionnel est obligatoire.");
        }
        if (repository.existsByMissionIdAndPrestataireId(missionId, professionnel)) {
            throw new IllegalStateException("Ce professionnel a déjà été invité.");
        }
        return repository.save(new Invitation(mission, professionnel, saisie.getMessage()));
    }

    public List<Invitation> parMission(Long missionId) {
        missionService.rechercher(missionId);
        return repository.findByMissionIdOrderById(missionId);
    }

    public List<Invitation> boiteDeReception(Long professionnelId) {
        return repository.findByPrestataireIdOrderByIdDesc(professionnelId);
    }

    @Transactional
    public List<Invitation> inviterAutomatiquement(Long missionId) {
        Mission mission = missionOuverte(missionId);
        return offreService.compatibles(mission).stream()
                .map(OffrePrestation::getPrestataireId)
                .distinct()
                .filter(id -> !repository.existsByMissionIdAndPrestataireId(missionId, id))
                .map(id -> repository.save(new Invitation(mission, id, "Une mission correspond à ton offre.")))
                .toList();
    }

    @Transactional
    public Invitation repondre(Long id, Invitation saisie) {
        missionOuvertePourInvitation(id);
        Invitation invitation = rechercher(id);
        exigerEnAttente(invitation);
        StatutInvitation statut = saisie.getStatut();
        BigDecimal tarif = saisie.getTarifPropose();
        if (statut == StatutInvitation.ACCEPTEE && tarif != null && tarif.signum() >= 0) {
            invitation.repondre(statut, tarif);
        } else if (statut == StatutInvitation.REFUSEE && tarif == null) {
            invitation.repondre(statut, null);
        } else {
            throw new IllegalArgumentException("Indique ACCEPTEE avec un tarif positif ou nul, ou REFUSEE sans tarif.");
        }
        return invitation;
    }

    @Transactional
    public Invitation selectionner(Long id) {
        Mission mission = missionOuvertePourInvitation(id);
        Invitation invitation = rechercher(id);
        if (invitation.getStatut() != StatutInvitation.ACCEPTEE || invitation.getTarifPropose() == null) {
            throw new IllegalStateException("Seule une réponse acceptée avec un tarif peut être retenue.");
        }
        Integer places = mission.getNombreProfessionnels();
        long retenus = repository.countByMissionIdAndStatut(mission.getId(), StatutInvitation.RETENUE);
        if (places == null || retenus >= places) {
            throw new IllegalStateException("Aucune place disponible pour cette mission.");
        }
        invitation.repondre(StatutInvitation.RETENUE, invitation.getTarifPropose());
        if (retenus + 1 == places) {
            mission.changerStatut(StatutMission.ATTRIBUEE);
        }
        return invitation;
    }

    private Mission missionOuverte(Long id) {
        Mission mission = missionService.verrouiller(id);
        missionService.exigerStatut(mission, StatutMission.OUVERTE, "La mission doit être publiée et ouverte.");
        return mission;
    }

    private Mission missionOuvertePourInvitation(Long id) {
        Long missionId = repository.rechercherMissionId(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Invitation introuvable (id=" + id + ")"));
        return missionOuverte(missionId);
    }

    private void exigerEnAttente(Invitation invitation) {
        if (invitation.getStatut() != StatutInvitation.EN_ATTENTE) {
            throw new IllegalStateException("L’invitation a déjà été traitée.");
        }
    }

    @Transactional
    public Invitation modifier(Long id, Invitation saisie) {
        missionOuvertePourInvitation(id);
        Invitation entite = rechercher(id);
        exigerEnAttente(entite);
        entite.modifier(saisie.getMessage());
        return entite;
    }

    @Transactional
    public void supprimer(Long id) {
        missionOuvertePourInvitation(id);
        Invitation invitation = rechercher(id);
        exigerEnAttente(invitation);
        repository.delete(invitation);
    }
}
