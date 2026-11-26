
package com.guideme.ms6posts.entity;

import com.guideme.ms6posts.enums.StatutPost;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;

    private Long idAuteur;

    private String titre;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private String imageUrl;

    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    private StatutPost statut;

    // Constructors
    public Post() {
    }

    // Getters and Setters

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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public StatutPost getStatut() {
        return statut;
    }

    public void setStatut(StatutPost statut) {
        this.statut = statut;
    }
}
