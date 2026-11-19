package com.guidini.ms2.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Objects;

/**
 * Contrat a raccorder a Spring Security/JWT : principal.name = identifiant MS1,
 * roles ADMIN ou PRESTATAIRE. Aucun identifiant fourni par le client n'est utilise.
 */
final class AccesUtilisateur {
    private AccesUtilisateur() {}

    static Long identifiant(HttpServletRequest request) {
        if (request.getUserPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentification requise.");
        }
        try {
            long id = Long.parseLong(request.getUserPrincipal().getName());
            if (id <= 0) throw new NumberFormatException();
            return id;
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiant MS1 absent du principal.");
        }
    }

    static Long gestionnaire(HttpServletRequest request) {
        Long id = identifiant(request);
        if (!request.isUserInRole("ADMIN") && !request.isUserInRole("PRESTATAIRE")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role prestataire ou administrateur requis.");
        }
        return id;
    }

    static Long administrateur(HttpServletRequest request) {
        Long id = identifiant(request);
        if (!request.isUserInRole("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role administrateur requis.");
        }
        return id;
    }

    static void proprietaire(HttpServletRequest request, Long proprietaireId) {
        Long id = gestionnaire(request);
        if (!request.isUserInRole("ADMIN") && !Objects.equals(id, proprietaireId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cette ressource appartient a un autre utilisateur.");
        }
    }
}

