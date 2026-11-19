package com.guidini.ms2.web.controller;

import com.guidini.ms2.domain.*;
import com.guidini.ms2.service.ProduitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@Transactional
public class ProduitController {
    private final ProduitService service;

    public ProduitController(ProduitService service) {
        this.service = service;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Produit> consulterPublications() {
        return ChargementReponse.produits(service.consulterPublications());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public Produit consulterPublication(@PathVariable Long id) {
        return ChargementReponse.produit(service.consulterPublication(id));
    }

    @GetMapping("/mes-publications")
    @Transactional(readOnly = true)
    public List<Produit> consulterMesPublications(HttpServletRequest request) {
        return ChargementReponse.produits(service.consulterParProprietaire(
                AccesUtilisateur.gestionnaire(request)));
    }

    @GetMapping("/demandes-publication")
    @Transactional(readOnly = true)
    public List<Produit> consulterDemandes(HttpServletRequest request) {
        AccesUtilisateur.administrateur(request);
        return ChargementReponse.produits(service.consulterDemandesPublication());
    }

    @GetMapping("/{id}/gestion")
    @Transactional(readOnly = true)
    public Produit consulterPourGestion(@PathVariable Long id, HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.produit(service.consulterProduit(id));
    }

    @PostMapping
    public ResponseEntity<Produit> creer(@RequestBody Produit donnees, HttpServletRequest request) {
        Produit cree = service.creerProduit(donnees, AccesUtilisateur.gestionnaire(request));
        return reponseCreation(cree);
    }

    @PutMapping("/{id}")
    public Produit modifier(@PathVariable Long id, @RequestBody Produit donnees,
            HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.produit(service.modifierProduit(id, donnees));
    }

    @PostMapping("/{id}/dupliquer")
    public ResponseEntity<Produit> dupliquer(@PathVariable Long id, HttpServletRequest request) {
        verifierAcces(id, request);
        return reponseCreation(service.dupliquerProduit(id, AccesUtilisateur.gestionnaire(request)));
    }

    @PatchMapping("/{id}/archiver")
    public Produit archiver(@PathVariable Long id, HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.produit(service.archiverProduit(id));
    }

    @PostMapping("/{id}/soumettre")
    public Produit soumettre(@PathVariable Long id, HttpServletRequest request) {
        verifierAcces(id, request);
        return ChargementReponse.produit(service.soumettreValidation(id));
    }

    @PostMapping("/{id}/accepter")
    public Produit accepter(@PathVariable Long id, HttpServletRequest request) {
        return ChargementReponse.produit(service.accepterPublication(id,
                AccesUtilisateur.administrateur(request)));
    }

    @PostMapping("/{id}/refuser")
    public Produit refuser(@PathVariable Long id, @RequestParam String motif,
            HttpServletRequest request) {
        return ChargementReponse.produit(service.refuserPublication(id,
                AccesUtilisateur.administrateur(request), motif));
    }

    @PostMapping("/{id}/publier")
    public Produit publierDirectement(@PathVariable Long id, HttpServletRequest request) {
        return ChargementReponse.produit(service.publierDirectement(id,
                AccesUtilisateur.administrateur(request)));
    }

    private void verifierAcces(Long id, HttpServletRequest request) {
        AccesUtilisateur.gestionnaire(request);
        AccesUtilisateur.proprietaire(request, service.consulterProduit(id).getProprietaireId());
    }

    private ResponseEntity<Produit> reponseCreation(Produit cree) {
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/produits/{id}/gestion").buildAndExpand(cree.getIdProduit()).toUri())
                .body(ChargementReponse.produit(cree));
    }

    @GetMapping("/recherche")
    @Transactional(readOnly = true)
    public List<Produit> rechercher(@RequestParam String destination) {
        return ChargementReponse.produits(service.rechercherParDestination(destination));
    }

    @GetMapping("/categorie/{categorie}")
    @Transactional(readOnly = true)
    public List<Produit> filtrer(@PathVariable CategorieProduit categorie) {
        return ChargementReponse.produits(service.filtrerParCategorie(categorie));
    }
}

