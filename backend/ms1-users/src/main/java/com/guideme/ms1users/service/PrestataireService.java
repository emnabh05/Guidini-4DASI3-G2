package com.guideme.ms1users.service;

import com.guideme.ms1users.dto.PrestataireRequest;
import com.guideme.ms1users.dto.PrestataireResponse;
import com.guideme.ms1users.entity.Prestataire;
import com.guideme.ms1users.entity.Role;
import com.guideme.ms1users.entity.Utilisateur;
import com.guideme.ms1users.exception.InvalidRoleException;
import com.guideme.ms1users.exception.ProfilAlreadyExistsException;
import com.guideme.ms1users.exception.ResourceNotFoundException;
import com.guideme.ms1users.repository.PrestataireRepository;
import com.guideme.ms1users.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PrestataireService {

    private final PrestataireRepository repository;
    private final UtilisateurRepository utilisateurRepository;

    public PrestataireResponse create(Long idUtilisateur, PrestataireRequest req) {
        Utilisateur u = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + idUtilisateur));
        if (u.getRole() != Role.PRESTATAIRE) {
            throw new InvalidRoleException("Seul un utilisateur PRESTATAIRE peut avoir un profil prestataire");
        }
        if (repository.existsByUtilisateurIdUtilisateur(idUtilisateur)) {
            throw new ProfilAlreadyExistsException(idUtilisateur);
        }
        Prestataire p = Prestataire.builder()
                .raisonSociale(req.raisonSociale())
                .matriculeFiscal(req.matriculeFiscal())
                .adresse(req.adresse())
                .description(req.description())
                .utilisateur(u)
                .build();
        return toResponse(repository.save(p));
    }

    @Transactional(readOnly = true)
    public PrestataireResponse get(Long idUtilisateur) {
        return toResponse(findOrThrow(idUtilisateur));
    }

    public PrestataireResponse update(Long idUtilisateur, PrestataireRequest req) {
        Prestataire p = findOrThrow(idUtilisateur);
        p.setRaisonSociale(req.raisonSociale());
        p.setMatriculeFiscal(req.matriculeFiscal());
        p.setAdresse(req.adresse());
        p.setDescription(req.description());
        return toResponse(repository.save(p));
    }

    public void delete(Long idUtilisateur) {
        repository.delete(findOrThrow(idUtilisateur));
    }

    private Prestataire findOrThrow(Long idUtilisateur) {
        return repository.findByUtilisateurIdUtilisateur(idUtilisateur)
                .orElseThrow(() -> new ResourceNotFoundException("Profil prestataire introuvable pour l'utilisateur : " + idUtilisateur));
    }

    private PrestataireResponse toResponse(Prestataire p) {
        return new PrestataireResponse(p.getIdAgence(), p.getUtilisateur().getIdUtilisateur(),
                p.getRaisonSociale(), p.getMatriculeFiscal(), p.getAdresse(), p.getDescription());
    }
}