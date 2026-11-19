package com.guideme.ms4missionsoffers.web.controller;

import com.guideme.ms4missionsoffers.domain.Mission;
import com.guideme.ms4missionsoffers.service.MissionService;
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
@RequestMapping("/api/missions")
public class MissionController {
    private final MissionService service;

    public MissionController(MissionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Mission> lister() {
        return service.lister();
    }

    @GetMapping("/{id}")
    public Mission rechercher(@PathVariable Long id) {
        return service.rechercher(id);
    }

    @PostMapping
    public ResponseEntity<Mission> creer(@RequestBody Mission saisie) {
        Mission entite = service.creer(saisie);
        return ResponseEntity.created(URI.create("/api/missions/" + entite.getId())).body(entite);
    }

    @PutMapping("/{id}")
    public Mission modifier(@PathVariable Long id, @RequestBody Mission saisie) {
        return service.modifier(id, saisie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publier")
    public Mission publier(@PathVariable Long id) {
        return service.publier(id);
    }

    @PostMapping("/{id}/annuler")
    public Mission annuler(@PathVariable Long id) {
        return service.annuler(id);
    }

    @PostMapping("/{id}/terminer")
    public Mission terminer(@PathVariable Long id) {
        return service.terminer(id);
    }
}
