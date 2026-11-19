package com.guideme.ms4missionsoffers.web.controller;

import com.guideme.ms4missionsoffers.domain.Invitation;
import com.guideme.ms4missionsoffers.service.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {
    private final InvitationService service;

    public InvitationController(InvitationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Invitation> lister(@RequestParam(required = false) Long missionId) {
        return missionId == null ? service.lister() : service.parMission(missionId);
    }

    @GetMapping("/{id}")
    public Invitation rechercher(@PathVariable Long id) {
        return service.rechercher(id);
    }

    @PostMapping
    public ResponseEntity<Invitation> creer(@RequestParam Long missionId, @RequestBody Invitation saisie) {
        Invitation entite = service.creer(missionId, saisie);
        return ResponseEntity.created(URI.create("/api/invitations/" + entite.getId())).body(entite);
    }

    @PutMapping("/{id}")
    public Invitation modifier(@PathVariable Long id, @RequestBody Invitation saisie) {
        return service.modifier(id, saisie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notifications")
    public List<Invitation> notifications(@RequestParam Long professionnelId) {
        return service.boiteDeReception(professionnelId);
    }

    @PostMapping("/automatiques")
    public ResponseEntity<List<Invitation>> inviterAutomatiquement(@RequestParam Long missionId) {
        return ResponseEntity.status(201).body(service.inviterAutomatiquement(missionId));
    }

    @PostMapping("/{id}/reponse")
    public Invitation repondre(@PathVariable Long id, @RequestBody Invitation saisie) {
        return service.repondre(id, saisie);
    }

    @PostMapping("/{id}/selection")
    public Invitation selectionner(@PathVariable Long id) {
        return service.selectionner(id);
    }
}
