package com.guidini.ms2.service;

import com.guidini.ms2.domain.*;
import com.guidini.ms2.exception.RessourceIntrouvableException;
import com.guidini.ms2.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * Operations metier. Les identifiants des acteurs doivent provenir du contexte
 * authentifie ; les autorisations administrateur seront raccordees avec JWT.
 */
@Service
@Transactional(readOnly = true)
public class ProduitService {
    private final ProduitRepository repository;

    public ProduitService(ProduitRepository repository) {
        this.repository = repository;
    }

    public Produit consulterProduit(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RessourceIntrouvableException("Produit introuvable : " + id));
    }

    public List<Produit> consulterPublications() {
        return repository.findByStatut(StatutPublication.PUBLIEE);
    }

    public Produit consulterPublication(Long id) {
        return repository.findByIdProduitAndStatut(id, StatutPublication.PUBLIEE).orElseThrow(
                () -> new RessourceIntrouvableException("Publication introuvable : " + id));
    }

    public List<Produit> consulterParProprietaire(Long proprietaireId) {
        return repository.findByProprietaireId(proprietaireId);
    }

    public List<Produit> consulterDemandesPublication() {
        return repository.findByStatut(StatutPublication.EN_ATTENTE);
    }

    @Transactional
    public Produit creerProduit(Produit donnees, Long auteurId) {
        ValidationMetier.identifiant(auteurId);
        ValidationMetier.produit(donnees, false);
        Produit cible = new Produit();
        copierChamps(donnees, cible);
        cible.setCreatedBy(auteurId);
        cible.setProprietaireId(auteurId);
        for (EtapeCircuit e : donnees.getEtapes()) {
            cible.ajouterEtape(EtapeCircuitService.copierEtape(e));
        }
        return repository.save(cible);
    }

    @Transactional
    public Produit modifierProduit(Long id, Produit donnees) {
        Produit cible = consulterProduit(id);
        ValidationMetier.modifiable(cible.getStatut());
        ValidationMetier.produit(donnees, false);
        copierChamps(donnees, cible);
        ValidationMetier.produit(cible, false);
        remettreEnBrouillon(cible);
        return repository.save(cible);
    }

    @Transactional
    public Produit dupliquerProduit(Long id, Long auteurId) {
        return creerProduit(consulterProduit(id), auteurId);
    }

    @Transactional
    public Produit archiverProduit(Long id) {
        Produit cible = consulterProduit(id);
        cible.setStatut(StatutPublication.ARCHIVEE);
        return repository.save(cible);
    }

    @Transactional
    public Produit soumettreValidation(Long id) {
        Produit cible = consulterProduit(id);
        ValidationMetier.modifiable(cible.getStatut());
        ValidationMetier.produit(cible, true);
        cible.setStatut(StatutPublication.EN_ATTENTE);
        cible.setValidatedBy(null);
        cible.setMotifRefus(null);
        return repository.save(cible);
    }

    @Transactional
    public Produit accepterPublication(Long id, Long administrateurId) {
        ValidationMetier.identifiant(administrateurId);
        Produit cible = consulterProduit(id);
        ValidationMetier.enAttente(cible.getStatut());
        ValidationMetier.produit(cible, true);
        return publier(cible, administrateurId);
    }

    @Transactional
    public Produit refuserPublication(Long id, Long administrateurId, String motif) {
        ValidationMetier.identifiant(administrateurId);
        ValidationMetier.texte(motif, "Motif du refus");
        Produit cible = consulterProduit(id);
        ValidationMetier.enAttente(cible.getStatut());
        cible.setStatut(StatutPublication.REFUSEE);
        cible.setValidatedBy(administrateurId);
        cible.setMotifRefus(motif.trim());
        return repository.save(cible);
    }

    @Transactional
    public Produit publierDirectement(Long id, Long administrateurId) {
        ValidationMetier.identifiant(administrateurId);
        Produit cible = consulterProduit(id);
        ValidationMetier.modifiable(cible.getStatut());
        ValidationMetier.exiger(Objects.equals(cible.getCreatedBy(), administrateurId)
                && Objects.equals(cible.getProprietaireId(), administrateurId),
                "La publication directe est reservee aux propres creations de l'administrateur.");
        ValidationMetier.produit(cible, true);
        return publier(cible, administrateurId);
    }

    private Produit publier(Produit cible, Long administrateurId) {
        cible.setStatut(StatutPublication.PUBLIEE);
        cible.setValidatedBy(administrateurId);
        cible.setMotifRefus(null);
        return repository.save(cible);
    }

    private void remettreEnBrouillon(Produit cible) {
        cible.setStatut(StatutPublication.BROUILLON);
        cible.setValidatedBy(null);
        cible.setMotifRefus(null);
    }

    // Seuls les champs editables sont copies : ni identite, ni audit, ni statut.
    private void copierChamps(Produit source, Produit cible) {
        cible.setTitre(source.getTitre());
        cible.setDescription(source.getDescription());
        cible.setCategorie(source.getCategorie());
        cible.setSousCategorie(source.getSousCategorie());
        cible.setDestination(source.getDestination());
        cible.setAdresse(source.getAdresse());
        cible.setPointRendezVous(source.getPointRendezVous());
        cible.setLieuDepart(source.getLieuDepart());
        cible.setLieuArrivee(source.getLieuArrivee());
        cible.setDureeMinutes(source.getDureeMinutes());
        cible.setDureeJours(source.getDureeJours());
        cible.setDistanceKm(source.getDistanceKm());
        cible.setTransport(source.getTransport());
        cible.setNiveauDifficulte(source.getNiveauDifficulte());
        cible.setPrixAdulte(source.getPrixAdulte());
        cible.setPrixEnfant(source.getPrixEnfant());
        cible.setCapaciteMin(source.getCapaciteMin());
        cible.setCapaciteMax(source.getCapaciteMax());
        cible.setAgeMinimum(source.getAgeMinimum());
        cible.setLangues(source.getLangues());
        cible.setAccessiblePMR(source.getAccessiblePMR());
        cible.setInclus(source.getInclus());
        cible.setNonInclus(source.getNonInclus());
        cible.setConditions(source.getConditions());
        cible.setEquipement(source.getEquipement());
        cible.setImagePrincipale(source.getImagePrincipale());
        cible.setVideoUrl(source.getVideoUrl());
    }

    public List<Produit> rechercherParDestination(String destination) {
        ValidationMetier.texte(destination, "Destination");
        return repository.findByStatutAndDestinationContainingIgnoreCase(
                StatutPublication.PUBLIEE, destination.trim());
    }

    public List<Produit> filtrerParCategorie(CategorieProduit categorie) {
        ValidationMetier.exiger(categorie != null, "Categorie obligatoire.");
        return repository.findByStatutAndCategorie(StatutPublication.PUBLIEE, categorie);
    }
}

