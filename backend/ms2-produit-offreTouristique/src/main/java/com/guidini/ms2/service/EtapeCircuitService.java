package com.guidini.ms2.service;

import com.guidini.ms2.domain.*;
import com.guidini.ms2.exception.RessourceIntrouvableException;
import com.guidini.ms2.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class EtapeCircuitService {
    private final EtapeCircuitRepository repository;
    private final ProduitRepository produitRepository;

    public EtapeCircuitService(EtapeCircuitRepository repository, ProduitRepository produitRepository) {
        this.repository = repository;
        this.produitRepository = produitRepository;
    }

    public EtapeCircuit consulterEtapeParId(Long idEtape) {
        return repository.findById(idEtape).orElseThrow(
                () -> new RessourceIntrouvableException("Etape introuvable : " + idEtape));
    }

    public List<EtapeCircuit> consulterEtapes(Long idProduit) {
        chargerCircuit(idProduit);
        return repository.findByProduit_IdProduitOrderByNumeroJourAscOrdreAsc(idProduit);
    }

    public EtapeCircuit consulterEtape(Long idProduit, Long idEtape) {
        return repository.findByIdEtapeAndProduit_IdProduit(idEtape, idProduit).orElseThrow(
                () -> new RessourceIntrouvableException("Etape introuvable dans ce circuit."));
    }

    @Transactional
    public EtapeCircuit ajouterEtape(Long idProduit, EtapeCircuit donnees) {
        Produit circuit = chargerCircuit(idProduit);
        ValidationMetier.modifiable(circuit.getStatut());
        ValidationMetier.etape(donnees, circuit);
        verifierPosition(circuit, donnees, null);
        EtapeCircuit etape = copierEtape(donnees);
        circuit.ajouterEtape(etape);
        marquerModification(circuit);
        return repository.save(etape);
    }

    @Transactional
    public EtapeCircuit modifierEtape(Long idProduit, Long idEtape, EtapeCircuit donnees) {
        Produit circuit = chargerCircuit(idProduit);
        ValidationMetier.modifiable(circuit.getStatut());
        EtapeCircuit cible = consulterEtape(idProduit, idEtape);
        ValidationMetier.etape(donnees, circuit);
        verifierPosition(circuit, donnees, idEtape);
        copierChamps(donnees, cible);
        marquerModification(circuit);
        return repository.save(cible);
    }

    @Transactional
    public void supprimerEtape(Long idProduit, Long idEtape) {
        Produit circuit = chargerCircuit(idProduit);
        ValidationMetier.modifiable(circuit.getStatut());
        EtapeCircuit etape = consulterEtape(idProduit, idEtape);
        circuit.supprimerEtape(etape);
        marquerModification(circuit);
        // orphanRemoval supprime l'etape au commit de la transaction.
    }

    @Transactional
    public EtapeCircuit changerOrdre(Long idProduit, Long idEtape, Integer numeroJour, Integer ordre) {
        EtapeCircuit donnees = copierEtape(consulterEtape(idProduit, idEtape));
        donnees.setNumeroJour(numeroJour);
        donnees.setOrdre(ordre);
        return modifierEtape(idProduit, idEtape, donnees);
    }

    private Produit chargerCircuit(Long idProduit) {
        Produit produit = produitRepository.findById(idProduit).orElseThrow(
                () -> new RessourceIntrouvableException("Circuit introuvable : " + idProduit));
        ValidationMetier.exiger(produit.getCategorie() == CategorieProduit.CIRCUIT,
                "Le produit doit etre un circuit.");
        return produit;
    }

    private void verifierPosition(Produit circuit, EtapeCircuit donnees, Long idExclu) {
        boolean occupee = circuit.getEtapes().stream()
                .filter(e -> idExclu == null || !Objects.equals(e.getIdEtape(), idExclu))
                .anyMatch(e -> Objects.equals(e.getNumeroJour(), donnees.getNumeroJour())
                        && Objects.equals(e.getOrdre(), donnees.getOrdre()));
        ValidationMetier.exiger(!occupee, "Cette position est deja occupee pour ce jour.");
    }

    private void marquerModification(Produit circuit) {
        circuit.setStatut(StatutPublication.BROUILLON);
        circuit.setValidatedBy(null);
        circuit.setMotifRefus(null);
        circuit.setUpdatedAt(LocalDateTime.now());
    }

    static EtapeCircuit copierEtape(EtapeCircuit source) {
        EtapeCircuit cible = new EtapeCircuit();
        copierChamps(source, cible);
        return cible;
    }

    private static void copierChamps(EtapeCircuit source, EtapeCircuit cible) {
        cible.setNumeroJour(source.getNumeroJour());
        cible.setOrdre(source.getOrdre());
        cible.setLieu(source.getLieu());
        cible.setHeureDebut(source.getHeureDebut());
        cible.setHeureFin(source.getHeureFin());
        cible.setDistanceKm(source.getDistanceKm());
        cible.setDescription(source.getDescription());
    }
}
