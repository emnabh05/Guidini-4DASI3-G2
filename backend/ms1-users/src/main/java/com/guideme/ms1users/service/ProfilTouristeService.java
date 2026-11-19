package com.guideme.ms1users.service;

import com.guideme.ms1users.dto.ProfilTouristeRequest;
import com.guideme.ms1users.dto.ProfilTouristeResponse;
import com.guideme.ms1users.entity.ProfilTouriste;
import com.guideme.ms1users.entity.Role;
import com.guideme.ms1users.entity.Utilisateur;
import com.guideme.ms1users.exception.InvalidRoleException;
import com.guideme.ms1users.exception.ProfilAlreadyExistsException;
import com.guideme.ms1users.exception.ResourceNotFoundException;
import com.guideme.ms1users.repository.ProfilTouristeRepository;
import com.guideme.ms1users.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilTouristeService {

    private final ProfilTouristeRepository repository;
    private final UtilisateurRepository utilisateurRepository;

    public ProfilTouristeResponse create(Long idUtilisateur, ProfilTouristeRequest req) {
        Utilisateur u = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + idUtilisateur));
        if (u.getRole() != Role.TOURISTE) {
            throw new InvalidRoleException("Seul un utilisateur TOURISTE peut avoir un profil touriste");
        }
        if (repository.existsByUtilisateurIdUtilisateur(idUtilisateur)) {
            throw new ProfilAlreadyExistsException(idUtilisateur);
        }
        ProfilTouriste p = ProfilTouriste.builder()
                .preferences(req.preferences())
                .pays(req.pays())
                .langue(req.langue())
                .utilisateur(u)
                .build();
        return toResponse(repository.save(p));
    }

    @Transactional(readOnly = true)
    public ProfilTouristeResponse get(Long idUtilisateur) {
        return toResponse(findOrThrow(idUtilisateur));
    }

    public ProfilTouristeResponse update(Long idUtilisateur, ProfilTouristeRequest req) {
        ProfilTouriste p = findOrThrow(idUtilisateur);
        p.setPreferences(req.preferences());
        p.setPays(req.pays());
        p.setLangue(req.langue());
        return toResponse(repository.save(p));
    }

    public void delete(Long idUtilisateur) {
        repository.delete(findOrThrow(idUtilisateur));
    }

    private ProfilTouriste findOrThrow(Long idUtilisateur) {
        return repository.findByUtilisateurIdUtilisateur(idUtilisateur)
                .orElseThrow(() -> new ResourceNotFoundException("Profil touriste introuvable pour l'utilisateur : " + idUtilisateur));
    }

    private ProfilTouristeResponse toResponse(ProfilTouriste p) {
        return new ProfilTouristeResponse(p.getIdProfil(), p.getUtilisateur().getIdUtilisateur(),
                p.getPreferences(), p.getPays(), p.getLangue());
    }
}