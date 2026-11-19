package com.guideme.ms1users.controller;

import com.guideme.ms1users.dto.DocumentResponse;
import com.guideme.ms1users.dto.ValidationRequest;
import com.guideme.ms1users.entity.StatutVerification;
import com.guideme.ms1users.service.DocumentProfessionnelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/documents")
@RequiredArgsConstructor
public class AdminDocumentController {

    private final DocumentProfessionnelService service;

    // ex: GET /api/admin/documents?statut=EN_ATTENTE
    @GetMapping
    public List<DocumentResponse> lister(@RequestParam(required = false) StatutVerification statut) {
        return service.listerParStatut(statut);
    }

    @PutMapping("/{id}/validation")
    public DocumentResponse valider(@PathVariable Long id, @Valid @RequestBody ValidationRequest req) {
        return service.valider(id, req);
    }
}