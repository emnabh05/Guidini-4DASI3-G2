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
public class OffreProduitService {
    private final OffreProduitRepository repository;
    private final ProduitRepository produitRepository;

    public OffreProduitService(OffreProduitRepository repository, ProduitRepository produitRepository) {
        this.repository = repository;
        this.produitRepository = produitRepository;
    }

    public OffreProduit consulterOffre(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RessourceIntrouvableException("Offre introuvable : " + id));
    }

    public List<OffreProduit> consulterPublications() {
        return repository.findByStatut(StatutPublication.PUBLIEE);
    }

    public OffreProduit consulterPublication(Long id) {
        return repository.findByIdOffreAndStatut(id, StatutPublication.PUBLIEE).orElseThrow(
                () -> new RessourceIntrouvableException("Publication introuvable : " + id));
    }

    public List<OffreProduit> consulterParProprietaire(Long proprietaireId) {
        return repository.findByProprietaireId(proprietaireId);
    }

    public List<OffreProduit> consulterDemandesPublication() {
        return repository.findByStatut(StatutPublication.EN_ATTENTE);
    }

    @Transactional
    public OffreProduit creerOffre(OffreProduit donnees, Long auteurId) {
        ValidationMetier.identifiant(auteurId);
        ValidationMetier.offre(donnees, false);
        OffreProduit cible = new OffreProduit();
        copierChamps(donnees, cible);
        cible.setCreatedBy(auteurId);
        cible.setProprietaireId(auteurId);
        for (Produit p : donnees.getProduits()) {
            ValidationMetier.exiger(p.getIdProduit() != null, "Identifiant produit obligatoire.");
            cible.ajouterProduit(chargerProduit(p.getIdProduit()));
        }
        return repository.save(cible);
    }

    @Transactional
    public OffreProduit modifierOffre(Long id, OffreProduit donnees) {
        OffreProduit cible = consulterOffre(id);
        ValidationMetier.modifiable(cible.getStatut());
        ValidationMetier.offre(donnees, false);
        copierChamps(donnees, cible);
        ValidationMetier.offre(cible, false);
        remettreEnBrouillon(cible);
        return repository.save(cible);
    }

    @Transactional
    public OffreProduit dupliquerOffre(Long id, Long auteurId) {
        return creerOffre(consulterOffre(id), auteurId);
    }

    @Transactional
    public OffreProduit archiverOffre(Long id) {
        OffreProduit cible = consulterOffre(id);
        cible.setStatut(StatutPublication.ARCHIVEE);
        return repository.save(cible);
    }

    @Transactional
    public OffreProduit soumettreValidation(Long id) {
        OffreProduit cible = consulterOffre(id);
        ValidationMetier.modifiable(cible.getStatut());
        ValidationMetier.offre(cible, true);
        cible.setStatut(StatutPublication.EN_ATTENTE);
        cible.setValidatedBy(null);
        cible.setMotifRefus(null);
        return repository.save(cible);
    }

    @Transactional
    public OffreProduit accepterPublication(Long id, Long administrateurId) {
        ValidationMetier.identifiant(administrateurId);
        OffreProduit cible = consulterOffre(id);
        ValidationMetier.enAttente(cible.getStatut());
        ValidationMetier.offre(cible, true);
        return publier(cible, administrateurId);
    }

    @Transactional
    public OffreProduit refuserPublication(Long id, Long administrateurId, String motif) {
        ValidationMetier.identifiant(administrateurId);
        ValidationMetier.texte(motif, "Motif du refus");
        OffreProduit cible = consulterOffre(id);
        ValidationMetier.enAttente(cible.getStatut());
        cible.setStatut(StatutPublication.REFUSEE);
        cible.setValidatedBy(administrateurId);
        cible.setMotifRefus(motif.trim());
        return repository.save(cible);
    }

    @Transactional
    public OffreProduit publierDirectement(Long id, Long administrateurId) {
        ValidationMetier.identifiant(administrateurId);
        OffreProduit cible = consulterOffre(id);
        ValidationMetier.modifiable(cible.getStatut());
        ValidationMetier.exiger(Objects.equals(cible.getCreatedBy(), administrateurId)
                && Objects.equals(cible.getProprietaireId(), administrateurId),
                "La publication directe est reservee aux propres creations de l'administrateur.");
        ValidationMetier.offre(cible, true);
        return publier(cible, administrateurId);
    }

    private OffreProduit publier(OffreProduit cible, Long administrateurId) {
        cible.setStatut(StatutPublication.PUBLIEE);
        cible.setValidatedBy(administrateurId);
        cible.setMotifRefus(null);
        return repository.save(cible);
    }

    private void remettreEnBrouillon(OffreProduit cible) {
        cible.setStatut(StatutPublication.BROUILLON);
        cible.setValidatedBy(null);
        cible.setMotifRefus(null);
    }

    // Seuls les champs editables sont copies : ni identite, ni audit, ni statut.
    private void copierChamps(OffreProduit source, OffreProduit cible) {
        cible.setTitre(source.getTitre());
        cible.setDescription(source.getDescription());
        cible.setPrixGlobal(source.getPrixGlobal());
        cible.setDateDebut(source.getDateDebut());
        cible.setDateFin(source.getDateFin());
        cible.setInclus(source.getInclus());
        cible.setNonInclus(source.getNonInclus());
        cible.setConditions(source.getConditions());
    }

    @Transactional
    public OffreProduit ajouterProduit(Long idOffre, Long idProduit) {
        OffreProduit offre = consulterOffre(idOffre);
        ValidationMetier.modifiable(offre.getStatut());
        offre.ajouterProduit(chargerProduit(idProduit));
        remettreEnBrouillon(offre);
        return repository.save(offre);
    }

    @Transactional
    public OffreProduit retirerProduit(Long idOffre, Long idProduit) {
        OffreProduit offre = consulterOffre(idOffre);
        ValidationMetier.modifiable(offre.getStatut());
        Produit produit = offre.getProduits().stream()
                .filter(p -> Objects.equals(p.getIdProduit(), idProduit)).findFirst()
                .orElseThrow(() -> new RessourceIntrouvableException("Produit absent de l'offre."));
        offre.retirerProduit(produit);
        remettreEnBrouillon(offre);
        return repository.save(offre);
    }

    private Produit chargerProduit(Long id) {
        Produit produit = produitRepository.findById(id).orElseThrow(
                () -> new RessourceIntrouvableException("Produit introuvable : " + id));
        ValidationMetier.exiger(produit.getStatut() != StatutPublication.ARCHIVEE,
                "Un produit archive ne peut pas etre ajoute a une offre.");
        return produit;
    }
}

