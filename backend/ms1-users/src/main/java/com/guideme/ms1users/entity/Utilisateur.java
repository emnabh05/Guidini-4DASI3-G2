package com.guideme.ms1users.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ms1_utilisateurs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUtilisateur;

    // nullable : Keycloak yetzed ba3d (S8)
    @Column(unique = true)
    private String keycloakId;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutUtilisateur statut;

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateMaj;

    @PrePersist
    void onCreate() {
        dateCreation = LocalDateTime.now();
        dateMaj = dateCreation;
    }

    @PreUpdate
    void onUpdate() {
        dateMaj = LocalDateTime.now();
    }
}