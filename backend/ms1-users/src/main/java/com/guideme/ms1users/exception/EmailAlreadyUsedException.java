package com.guideme.ms1users.exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("Email déjà utilisé : " + email);
    }
}