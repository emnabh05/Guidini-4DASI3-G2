package com.guideme.ms1users.service;

import com.guideme.ms1users.dto.DocumentRequest;
import com.guideme.ms1users.dto.DocumentResponse;
import com.guideme.ms1users.dto.ValidationRequest;
import com.guideme.ms1users.entity.*;
import com.guideme.ms1users.exception.InvalidOperationException;
import com.guideme.ms1users.exception.InvalidRoleException;
import com.guideme.ms1users.exception.ResourceNotFoundException;
import com.guideme.ms1users.repository.DocumentProfessionnelRepository;
import com.guideme.ms1users.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentProfessionnelService {

    private final DocumentProfessionnelRepository repository;
    private final UtilisateurRepository utilisateurRepository;

    // Dépôt d'un document (étapes 5-8 et 16-19 du diagramme de séquence)
    public DocumentResponse deposer(DocumentRequest req) {
        Utilisateur u = findUtilisateur(req.idUtilisateur());
        if (u.getRole() == Role.TOURISTE || u.getRole() == Role.ADMIN) {
            throw new InvalidRoleException("Seuls un GUIDE, CHAUFFEUR ou PRESTATAIRE peuvent déposer des documents");
        }
        DocumentProfessionnel d = DocumentProfessionnel.builder()
                .typeDocument(req.typeDocument())
                .numeroDocument(req.numeroDocument())
                .dateExpiration(req.dateExpiration())
                .cheminFichier(req.cheminFichier())
                .statutVerification(StatutVerification.EN_ATTENTE)
                .utilisateur(u)
                .build();
        // Nouveau document après un refus : le compte repasse en attente de validation
        if (u.getStatut() == StatutUtilisateur.REFUSE) {
            u.setStatut(StatutUtilisateur.EN_ATTENTE_VALIDATION);
            utilisateurRepository.save(u);
        }
        return toResponse(repository.save(d));
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> mesDocuments(Long idUtilisateur) {
        findUtilisateur(idUtilisateur);
        return repository.findByUtilisateurIdUtilisateurOrderByDateDepotDesc(idUtilisateur)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> listerParStatut(StatutVerification statut) {
        List<DocumentProfessionnel> docs = (statut == null)
                ? repository.findAll()
                : repository.findByStatutVerificationOrderByDateDepotAsc(statut);
        return docs.stream().map(this::toResponse).toList();
    }

    // Validation / refus par l'admin (étapes 9-12 et 20-23)
    public DocumentResponse valider(Long idDocument, ValidationRequest req) {
        DocumentProfessionnel d = repository.findById(idDocument)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable : " + idDocument));

        if (req.decision() == StatutVerification.EN_ATTENTE) {
            throw new InvalidOperationException("La décision doit être VALIDE ou REFUSE");
        }
        if (d.getStatutVerification() != StatutVerification.EN_ATTENTE) {
            throw new InvalidOperationException("Ce document a déjà été traité : " + d.getStatutVerification());
        }

        Utilisateur u = d.getUtilisateur();
        d.setDateVerification(LocalDateTime.now());

        if (req.decision() == StatutVerification.VALIDE) {
            d.setStatutVerification(StatutVerification.VALIDE);
            d.setMotifRefus(null);
            u.setStatut(StatutUtilisateur.ACTIF);
        } else {
            if (req.motifRefus() == null || req.motifRefus().isBlank()) {
                throw new InvalidOperationException("Le motif de refus est obligatoire");
            }
            d.setStatutVerification(StatutVerification.REFUSE);
            d.setMotifRefus(req.motifRefus());
            u.setStatut(StatutUtilisateur.REFUSE);
        }
        utilisateurRepository.save(u);
        return toResponse(repository.save(d));
    }

    private Utilisateur findUtilisateur(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + id));
    }

    private DocumentResponse toResponse(DocumentProfessionnel d) {
        return new DocumentResponse(d.getIdDocument(), d.getUtilisateur().getIdUtilisateur(),
                d.getTypeDocument(), d.getNumeroDocument(), d.getDateExpiration(), d.getCheminFichier(),
                d.getStatutVerification(), d.getMotifRefus(), d.getDateDepot(), d.getDateVerification());
    }
}