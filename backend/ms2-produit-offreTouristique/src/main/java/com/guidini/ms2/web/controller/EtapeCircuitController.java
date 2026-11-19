package com.guidini.ms2.web.controller;

import com.guidini.ms2.domain.*;
import com.guidini.ms2.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class EtapeCircuitController {
    private final EtapeCircuitService service;
    private final ProduitService produitService;

    public EtapeCircuitController(EtapeCircuitService service, ProduitService produitService) {
        this.service = service;
        this.produitService = produitService;
    }

    @GetMapping("/produits/{idProduit}/etapes")
    @Transactional(readOnly = true)
    public List<EtapeCircuit> consulter(@PathVariable Long idProduit) {
        produitService.consulterPublication(idProduit);
        return service.consulterEtapes(idProduit);
    }

    @PostMapping("/produits/{idProduit}/etapes")
    public ResponseEntity<EtapeCircuit> ajouter(@PathVariable Long idProduit,
            @RequestBody EtapeCircuit donnees, HttpServletRequest request) {
        verifierAcces(idProduit, request);
        EtapeCircuit cree = service.ajouterEtape(idProduit, donnees);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/etapes/{idEtape}").buildAndExpand(cree.getIdEtape()).toUri()).body(cree);
    }

    @GetMapping("/etapes/{idEtape}")
    @Transactional(readOnly = true)
    public EtapeCircuit consulterEtape(@PathVariable Long idEtape, HttpServletRequest request) {
        EtapeCircuit etape = service.consulterEtapeParId(idEtape);
        Produit produit = etape.getProduit();
        if (produit.getStatut() != StatutPublication.PUBLIEE) {
            verifierAcces(produit.getIdProduit(), request);
        }
        return etape;
    }

    @PutMapping("/etapes/{idEtape}")
    public EtapeCircuit modifier(@PathVariable Long idEtape, @RequestBody EtapeCircuit donnees,
            HttpServletRequest request) {
        Long idProduit = circuitAutorise(idEtape, request);
        return service.modifierEtape(idProduit, idEtape, donnees);
    }

    @PatchMapping("/etapes/{idEtape}/ordre")
    public EtapeCircuit changerOrdre(@PathVariable Long idEtape, @RequestParam Integer numeroJour,
            @RequestParam Integer ordre, HttpServletRequest request) {
        Long idProduit = circuitAutorise(idEtape, request);
        return service.changerOrdre(idProduit, idEtape, numeroJour, ordre);
    }

    @DeleteMapping("/etapes/{idEtape}")
    public ResponseEntity<Void> supprimer(@PathVariable Long idEtape, HttpServletRequest request) {
        Long idProduit = circuitAutorise(idEtape, request);
        service.supprimerEtape(idProduit, idEtape);
        return ResponseEntity.noContent().build();
    }

    private Long circuitAutorise(Long idEtape, HttpServletRequest request) {
        AccesUtilisateur.gestionnaire(request);
        Long idProduit = service.consulterEtapeParId(idEtape).getIdProduit();
        verifierAcces(idProduit, request);
        return idProduit;
    }

    private void verifierAcces(Long idProduit, HttpServletRequest request) {
        AccesUtilisateur.gestionnaire(request);
        AccesUtilisateur.proprietaire(request, produitService.consulterProduit(idProduit).getProprietaireId());
    }
}

