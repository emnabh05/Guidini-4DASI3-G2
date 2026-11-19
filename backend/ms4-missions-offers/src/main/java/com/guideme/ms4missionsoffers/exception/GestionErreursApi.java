package com.guideme.ms4missionsoffers.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestionErreursApi {
    @ExceptionHandler(RessourceIntrouvableException.class)
    public ProblemDetail ressourceIntrouvable(RessourceIntrouvableException erreur) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, erreur.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail saisieInvalide(IllegalArgumentException erreur) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, erreur.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail contrainteDeDonnees() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "L’opération enfreint une contrainte de la base de données.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail etatInvalide(IllegalStateException erreur) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, erreur.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail modificationConcurrente() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "L’invitation a été modifiée simultanément.");
    }
}
