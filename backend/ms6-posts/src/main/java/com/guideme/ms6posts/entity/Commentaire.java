
package com.guideme.ms6posts.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "commentaires")
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCommentaire;

    private Long idPost;

    private Long idAuteur;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateCreation;

    private Long idCommentaireParent;

    // Constructeur vide
    public Commentaire() {
    }

    // Getters et Setters

    public Long getIdCommentaire() {
        return idCommentaire;
    }

    public void setIdCommentaire(Long idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    public Long getIdAuteur() {
        return idAuteur;
    }

    public void setIdAuteur(Long idAuteur) {
        this.idAuteur = idAuteur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getIdCommentaireParent() {
        return idCommentaireParent;
    }

    public void setIdCommentaireParent(Long idCommentaireParent) {
        this.idCommentaireParent = idCommentaireParent;
    }
}
