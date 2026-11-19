package com.guideme.ms1users.controller;

import com.guideme.ms1users.dto.UtilisateurRequest;
import com.guideme.ms1users.dto.UtilisateurResponse;
import com.guideme.ms1users.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService service;

    @PostMapping
    public ResponseEntity<UtilisateurResponse> create(@Valid @RequestBody UtilisateurRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping("/{id}")
    public UtilisateurResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<UtilisateurResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public UtilisateurResponse update(@PathVariable Long id,
                                      @Valid @RequestBody UtilisateurRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}