package com.guidini.ms2.web.controller;

import com.guidini.ms2.domain.*;
import com.guidini.ms2.service.OffreProduitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;

@RestController
@RequestMapping("/api/offres")
@Transactional
public class OffreController {
    private final OffreProduitService service;

    public OffreController(OffreProduitService service) {
        this.service = service;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<OffreProduit> consulterPublications() {
        return ChargementReponse.offres(service.consulterPublications());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public OffreProduit consulterPublication(@PathVariable Long id) {
        return ChargementReponse.offre(service.consulterPublication(id));
    }

    @GetMapping("/mes-publications")
    @Transactional(readOnly = true)
    public List<OffreProduit> consulterMesPublications(HttpServletRequest request) {
        return ChargementReponse.offres(service.consulterParProprietaire(
                AccesUtilisateur.gestionnaire(request)));
    }

    @GetMapping("/demandes-publication")
    @Transactional(readOnly = true)
    public List<OffreProduit> consulterDemandes(HttpServletRequest request) {
        AccesUtilisateur.administrateur(request);
        return ChargementReponse.offres(service.consulterDemandesPublication());
    }

    @GetMapping("/{id}/gestion")
    @Transactional(readOnly = true)
    public OffreProduit consulterPourGestion(@PathVariable Long id, HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.offre(service.consulterOffre(id));
    }

    @PostMapping
    public ResponseEntity<OffreProduit> creer(@RequestBody OffreProduit donnees, HttpServletRequest request) {
        OffreProduit cree = service.creerOffre(donnees, AccesUtilisateur.gestionnaire(request));
        return reponseCreation(cree);
    }

    @PutMapping("/{id}")
    public OffreProduit modifier(@PathVariable Long id, @RequestBody OffreProduit donnees,
            HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.offre(service.modifierOffre(id, donnees));
    }

    @PostMapping("/{id}/dupliquer")
    public ResponseEntity<OffreProduit> dupliquer(@PathVariable Long id, HttpServletRequest request) {
        verifierAcces(id, request);
        return reponseCreation(service.dupliquerOffre(id, AccesUtilisateur.gestionnaire(request)));
    }

    @PatchMapping("/{id}/archiver")
    public OffreProduit archiver(@PathVariable Long id, HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.offre(service.archiverOffre(id));
    }

    @PostMapping("/{id}/soumettre")
    public OffreProduit soumettre(@PathVariable Long id, HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.offre(service.soumettreValidation(id));
    }

    @PostMapping("/{id}/accepter")
    public OffreProduit accepter(@PathVariable Long id, HttpServletRequest request) {
        return ChargementReponse.offre(service.accepterPublication(id,
                AccesUtilisateur.administrateur(request)));
    }

    @PostMapping("/{id}/refuser")
    public OffreProduit refuser(@PathVariable Long id, @RequestParam String motif,
            HttpServletRequest request) {
        return ChargementReponse.offre(service.refuserPublication(id,
                AccesUtilisateur.administrateur(request), motif));
    }

    @PostMapping("/{id}/publier")
    public OffreProduit publierDirectement(@PathVariable Long id, HttpServletRequest request) {
        return ChargementReponse.offre(service.publierDirectement(id,
                AccesUtilisateur.administrateur(request)));
    }

    private void verifierAcces(Long id, HttpServletRequest request) {
        AccesUtilisateur.gestionnaire(request);
        AccesUtilisateur.proprietaire(request, service.consulterOffre(id).getProprietaireId());
    }

    private ResponseEntity<OffreProduit> reponseCreation(OffreProduit cree) {
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/offres/{id}/gestion").buildAndExpand(cree.getIdOffre()).toUri())
                .body(ChargementReponse.offre(cree));
    }

    @PostMapping("/{id}/produits/{idProduit}")
    public OffreProduit ajouterProduit(@PathVariable Long id, @PathVariable Long idProduit,
            HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.offre(service.ajouterProduit(id, idProduit));
    }

    @DeleteMapping("/{id}/produits/{idProduit}")
    public OffreProduit retirerProduit(@PathVariable Long id, @PathVariable Long idProduit,
            HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.offre(service.retirerProduit(id, idProduit));
    }
}

