package com.guideme.ms1users.service;

import com.guideme.ms1users.dto.UtilisateurRequest;
import com.guideme.ms1users.dto.UtilisateurResponse;
import com.guideme.ms1users.entity.Role;
import com.guideme.ms1users.entity.StatutUtilisateur;
import com.guideme.ms1users.entity.Utilisateur;
import com.guideme.ms1users.exception.EmailAlreadyUsedException;
import com.guideme.ms1users.exception.ResourceNotFoundException;
import com.guideme.ms1users.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository repository;

    public UtilisateurResponse create(UtilisateurRequest req) {
        if (repository.existsByEmail(req.email())) {
            throw new EmailAlreadyUsedException(req.email());
        }
        Utilisateur u = Utilisateur.builder()
                .nom(req.nom())
                .prenom(req.prenom())
                .email(req.email())
                .telephone(req.telephone())
                .role(req.role())
                .statut(statutInitial(req.role()))
                .build();
        return toResponse(repository.save(u));
    }

    @Transactional(readOnly = true)
    public UtilisateurResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<UtilisateurResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public UtilisateurResponse update(Long id, UtilisateurRequest req) {
        Utilisateur u = findOrThrow(id);
        if (repository.existsByEmailAndIdUtilisateurNot(req.email(), id)) {
            throw new EmailAlreadyUsedException(req.email());
        }
        u.setNom(req.nom());
        u.setPrenom(req.prenom());
        u.setEmail(req.email());
        u.setTelephone(req.telephone());
        u.setRole(req.role());
        return toResponse(repository.save(u));
    }

    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    // Touriste / Admin : actif direct. Guide / Chauffeur / Prestataire : validation des documents d'abord.
    private StatutUtilisateur statutInitial(Role role) {
        return (role == Role.TOURISTE || role == Role.ADMIN)
                ? StatutUtilisateur.ACTIF
                : StatutUtilisateur.EN_ATTENTE_VALIDATION;
    }

    private Utilisateur findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + id));
    }

    private UtilisateurResponse toResponse(Utilisateur u) {
        return new UtilisateurResponse(u.getIdUtilisateur(), u.getNom(), u.getPrenom(), u.getEmail(),
                u.getTelephone(), u.getRole(), u.getStatut(), u.getDateCreation(), u.getDateMaj());
    }
}