package com.guideme.ms1users.controller;

import com.guideme.ms1users.dto.ProfilTouristeRequest;
import com.guideme.ms1users.dto.ProfilTouristeResponse;
import com.guideme.ms1users.service.ProfilTouristeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs/{idUtilisateur}/profil-touriste")
@RequiredArgsConstructor
public class ProfilTouristeController {

    private final ProfilTouristeService service;

    @PostMapping
    public ResponseEntity<ProfilTouristeResponse> create(@PathVariable Long idUtilisateur,
                                                         @Valid @RequestBody ProfilTouristeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(idUtilisateur, req));
    }

    @GetMapping
    public ProfilTouristeResponse get(@PathVariable Long idUtilisateur) {
        return service.get(idUtilisateur);
    }

    @PutMapping
    public ProfilTouristeResponse update(@PathVariable Long idUtilisateur,
                                         @Valid @RequestBody ProfilTouristeRequest req) {
        return service.update(idUtilisateur, req);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idUtilisateur) {
        service.delete(idUtilisateur);
    }
}