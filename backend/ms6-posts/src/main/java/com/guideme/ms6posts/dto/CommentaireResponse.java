package com.guideme.ms6posts.dto;

import com.guideme.ms6posts.entity.Commentaire;

import java.time.LocalDateTime;

public record CommentaireResponse(
        Long idCommentaire,
        Long idPost,
        Long idAuteur,
        String contenu,
        LocalDateTime dateCreation,
        Long idCommentaireParent
) {
    public static CommentaireResponse from(Commentaire c) {
        return new CommentaireResponse(
                c.getIdCommentaire(), c.getIdPost(), c.getIdAuteur(), c.getContenu(),
                c.getDateCreation(), c.getIdCommentaireParent());
    }
}
