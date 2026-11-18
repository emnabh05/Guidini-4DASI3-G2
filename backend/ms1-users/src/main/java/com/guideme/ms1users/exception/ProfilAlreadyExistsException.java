package com.guideme.ms1users.exception;

public class ProfilAlreadyExistsException extends RuntimeException {
    public ProfilAlreadyExistsException(Long idUtilisateur) {
        super("Cet utilisateur a déjà un profil : " + idUtilisateur);
    }
}