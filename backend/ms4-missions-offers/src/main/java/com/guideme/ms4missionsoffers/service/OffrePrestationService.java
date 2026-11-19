package com.guideme.ms4missionsoffers.service;

import com.guideme.ms4missionsoffers.domain.OffrePrestation;
import com.guideme.ms4missionsoffers.domain.Mission;
import com.guideme.ms4missionsoffers.domain.StatutOffre;
import com.guideme.ms4missionsoffers.exception.RessourceIntrouvableException;
import com.guideme.ms4missionsoffers.repository.OffrePrestationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OffrePrestationService {
    private final OffrePrestationRepository repository;
    public OffrePrestationService(OffrePrestationRepository repository) {
        this.repository = repository;
    }

    public List<OffrePrestation> lister() {
        return repository.findAll();
    }

    public OffrePrestation rechercher(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Offre de prestation introuvable (id=" + id + ")"));
    }

    @Transactional
    public OffrePrestation creer(OffrePrestation saisie) {
        valider(saisie);
        if (saisie.getPrestataireId() == null || saisie.getPrestataireId() <= 0) {
            throw new IllegalArgumentException("Le professionnel est obligatoire.");
        }
        OffrePrestation offre = new OffrePrestation(saisie.getPrestataireId(), saisie.getTarifPropose(), saisie.getMessage());
        offre.definirDisponibilite(saisie);
        return repository.save(offre);
    }

    @Transactional
    public OffrePrestation modifier(Long id, OffrePrestation saisie) {
        valider(saisie);
        OffrePrestation entite = rechercher(id);
        entite.modifier(saisie.getTarifPropose(), saisie.getMessage());
        entite.definirDisponibilite(saisie);
        return entite;
    }

    @Transactional
    public void supprimer(Long id) {
        repository.delete(rechercher(id));
    }

    List<OffrePrestation> compatibles(Mission mission) {
        return repository.findByStatutAndLocalisationIgnoreCaseAndLangueIgnoreCaseAndTypePrestationAndDisponibleDuLessThanEqualAndDisponibleAuGreaterThanEqual(
                StatutOffre.PUBLIEE, mission.getLieu().strip(), mission.getLangue(), mission.getTypePrestation(),
                mission.getDateDebut(), mission.getDateFin());
    }

    private void valider(OffrePrestation saisie) {
        if (saisie.getServices() == null || saisie.getServices().isBlank()
                || saisie.getLocalisation() == null || saisie.getLocalisation().isBlank()
                || saisie.getLangue() == null || saisie.getLangue().isBlank()
                || saisie.getTypePrestation() == null || saisie.getDisponibleDu() == null
                || saisie.getDisponibleAu() == null || saisie.getDisponibleAu().isBefore(saisie.getDisponibleDu())) {
            throw new IllegalArgumentException("Renseigne les services, la localisation, la langue, le type et les disponibilités.");
        }
        if (saisie.getTarifPropose() == null || saisie.getTarifPropose().signum() < 0) {
            throw new IllegalArgumentException("Le tarif proposé doit être positif ou nul.");
        }
    }
}
