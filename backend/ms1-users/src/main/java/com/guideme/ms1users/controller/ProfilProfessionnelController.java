package com.guideme.ms1users.controller;

import com.guideme.ms1users.dto.ProfilProfessionnelRequest;
import com.guideme.ms1users.dto.ProfilProfessionnelResponse;
import com.guideme.ms1users.service.ProfilProfessionnelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs/{idUtilisateur}/profil-professionnel")
@RequiredArgsConstructor
public class ProfilProfessionnelController {

    private final ProfilProfessionnelService service;

    @PostMapping
    public ResponseEntity<ProfilProfessionnelResponse> create(@PathVariable Long idUtilisateur,
                                                              @Valid @RequestBody ProfilProfessionnelRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(idUtilisateur, req));
    }

    @GetMapping
    public ProfilProfessionnelResponse get(@PathVariable Long idUtilisateur) {
        return service.get(idUtilisateur);
    }

    @PutMapping
    public ProfilProfessionnelResponse update(@PathVariable Long idUtilisateur,
                                              @Valid @RequestBody ProfilProfessionnelRequest req) {
        return service.update(idUtilisateur, req);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idUtilisateur) {
        service.delete(idUtilisateur);
    }
}