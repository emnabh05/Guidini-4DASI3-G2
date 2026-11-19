package com.guideme.ms1users.controller;

import com.guideme.ms1users.dto.PrestataireRequest;
import com.guideme.ms1users.dto.PrestataireResponse;
import com.guideme.ms1users.service.PrestataireService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs/{idUtilisateur}/prestataire")
@RequiredArgsConstructor
public class PrestataireController {

    private final PrestataireService service;

    @PostMapping
    public ResponseEntity<PrestataireResponse> create(@PathVariable Long idUtilisateur,
                                                      @Valid @RequestBody PrestataireRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(idUtilisateur, req));
    }

    @GetMapping
    public PrestataireResponse get(@PathVariable Long idUtilisateur) {
        return service.get(idUtilisateur);
    }

    @PutMapping
    public PrestataireResponse update(@PathVariable Long idUtilisateur,
                                      @Valid @RequestBody PrestataireRequest req) {
        return service.update(idUtilisateur, req);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idUtilisateur) {
        service.delete(idUtilisateur);
    }
}