package com.guideme.ms1users.service;

import com.guideme.ms1users.dto.ProfessionnelResponse;
import com.guideme.ms1users.entity.*;
import com.guideme.ms1users.exception.InvalidOperationException;
import com.guideme.ms1users.repository.ProfilProfessionnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessionnelSearchService {

    private final ProfilProfessionnelRepository repository;

    public List<ProfessionnelResponse> rechercher(String type, String langue) {
        TypeProfessionnel t = null;
        if (type != null && !type.isBlank()) {
            try {
                t = TypeProfessionnel.valueOf(type.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidOperationException("Type invalide (guide ou chauffeur) : " + type);
            }
        }
        String l = (langue == null || langue.isBlank()) ? null : langue.trim();
        return repository.rechercher(StatutUtilisateur.ACTIF, Disponibilite.DISPONIBLE, t, l)
                .stream()
                .map(p -> new ProfessionnelResponse(
                        p.getUtilisateur().getIdUtilisateur(),
                        p.getUtilisateur().getNom(),
                        p.getUtilisateur().getPrenom(),
                        p.getTypeProfessionnel(), p.getDescription(),
                        p.getDisponibilite(), p.getLangues(), p.getExperienceAns()))
                .toList();
    }
}