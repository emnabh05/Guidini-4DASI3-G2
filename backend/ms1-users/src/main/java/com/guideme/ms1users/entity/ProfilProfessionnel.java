package com.guideme.ms1users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ms1_profils_professionnels")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProfilProfessionnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeProfessionnel typeProfessionnel;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Disponibilite disponibilite;

    private String langues;

    private Integer experienceAns;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_utilisateur", nullable = false, unique = true)
    private Utilisateur utilisateur;
}