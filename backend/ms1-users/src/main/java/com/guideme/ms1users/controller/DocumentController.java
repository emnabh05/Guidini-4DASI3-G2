package com.guideme.ms1users.controller;

import com.guideme.ms1users.dto.DocumentRequest;
import com.guideme.ms1users.dto.DocumentResponse;
import com.guideme.ms1users.service.DocumentProfessionnelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentProfessionnelService service;

    @PostMapping
    public ResponseEntity<DocumentResponse> deposer(@Valid @RequestBody DocumentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.deposer(req));
    }

    // Temporaire : idUtilisateur en paramètre, remplacé par le token Keycloak (S8)
    @GetMapping("/mes-documents")
    public List<DocumentResponse> mesDocuments(@RequestParam Long idUtilisateur) {
        return service.mesDocuments(idUtilisateur);
    }
}