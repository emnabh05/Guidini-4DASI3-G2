package com.guideme.ms1users.service;

import com.guideme.ms1users.dto.ProfilProfessionnelRequest;
import com.guideme.ms1users.dto.ProfilProfessionnelResponse;
import com.guideme.ms1users.entity.*;
import com.guideme.ms1users.exception.InvalidRoleException;
import com.guideme.ms1users.exception.ProfilAlreadyExistsException;
import com.guideme.ms1users.exception.ResourceNotFoundException;
import com.guideme.ms1users.repository.ProfilProfessionnelRepository;
import com.guideme.ms1users.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilProfessionnelService {

    private final ProfilProfessionnelRepository repository;
    private final UtilisateurRepository utilisateurRepository;

    public ProfilProfessionnelResponse create(Long idUtilisateur, ProfilProfessionnelRequest req) {
        Utilisateur u = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + idUtilisateur));
        if (u.getRole() != Role.GUIDE && u.getRole() != Role.CHAUFFEUR) {
            throw new InvalidRoleException("Seul un GUIDE ou un CHAUFFEUR peut avoir un profil professionnel");
        }
        if (repository.existsByUtilisateurIdUtilisateur(idUtilisateur)) {
            throw new ProfilAlreadyExistsException(idUtilisateur);
        }
        ProfilProfessionnel p = ProfilProfessionnel.builder()
                .typeProfessionnel(u.getRole() == Role.GUIDE ? TypeProfessionnel.GUIDE : TypeProfessionnel.CHAUFFEUR)
                .description(req.description())
                .disponibilite(req.disponibilite() != null ? req.disponibilite() : Disponibilite.DISPONIBLE)
                .langues(req.langues())
                .experienceAns(req.experienceAns())
                .utilisateur(u)
                .build();
        return toResponse(repository.save(p));
    }

    @Transactional(readOnly = true)
    public ProfilProfessionnelResponse get(Long idUtilisateur) {
        return toResponse(findOrThrow(idUtilisateur));
    }

    public ProfilProfessionnelResponse update(Long idUtilisateur, ProfilProfessionnelRequest req) {
        ProfilProfessionnel p = findOrThrow(idUtilisateur);
        p.setDescription(req.description());
        if (req.disponibilite() != null) p.setDisponibilite(req.disponibilite());
        p.setLangues(req.langues());
        p.setExperienceAns(req.experienceAns());
        return toResponse(repository.save(p));
    }

    public void delete(Long idUtilisateur) {
        repository.delete(findOrThrow(idUtilisateur));
    }

    private ProfilProfessionnel findOrThrow(Long idUtilisateur) {
        return repository.findByUtilisateurIdUtilisateur(idUtilisateur)
                .orElseThrow(() -> new ResourceNotFoundException("Profil professionnel introuvable pour l'utilisateur : " + idUtilisateur));
    }

    private ProfilProfessionnelResponse toResponse(ProfilProfessionnel p) {
        return new ProfilProfessionnelResponse(p.getIdProfil(), p.getUtilisateur().getIdUtilisateur(),
                p.getTypeProfessionnel(), p.getDescription(), p.getDisponibilite(),
                p.getLangues(), p.getExperienceAns());
    }
}