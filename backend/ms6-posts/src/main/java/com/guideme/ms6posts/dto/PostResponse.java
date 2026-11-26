package com.guideme.ms6posts.dto;

import com.guideme.ms6posts.entity.Post;
import com.guideme.ms6posts.enums.StatutPost;

import java.time.LocalDateTime;

public record PostResponse(
        Long idPost,
        Long idAuteur,
        String titre,
        String contenu,
        String imageUrl,
        LocalDateTime dateCreation,
        StatutPost statut
) {
    public static PostResponse from(Post p) {
        return new PostResponse(
                p.getIdPost(), p.getIdAuteur(), p.getTitre(), p.getContenu(),
                p.getImageUrl(), p.getDateCreation(), p.getStatut());
    }
}
