package com.guideme.ms4missionsoffers.web.controller;

import com.guideme.ms4missionsoffers.domain.OffrePrestation;
import com.guideme.ms4missionsoffers.service.OffrePrestationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/offres")
public class OffrePrestationController {
    private final OffrePrestationService service;

    public OffrePrestationController(OffrePrestationService service) {
        this.service = service;
    }

    @GetMapping
    public List<OffrePrestation> lister() {
        return service.lister();
    }

    @GetMapping("/{id}")
    public OffrePrestation rechercher(@PathVariable Long id) {
        return service.rechercher(id);
    }

    @PostMapping
    public ResponseEntity<OffrePrestation> creer(@RequestBody OffrePrestation saisie) {
        OffrePrestation entite = service.creer(saisie);
        return ResponseEntity.created(URI.create("/api/offres/" + entite.getId())).body(entite);
    }

    @PutMapping("/{id}")
    public OffrePrestation modifier(@PathVariable Long id, @RequestBody OffrePrestation saisie) {
        return service.modifier(id, saisie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
