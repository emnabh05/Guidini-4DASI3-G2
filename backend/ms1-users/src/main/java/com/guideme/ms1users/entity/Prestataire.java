package com.guideme.ms1users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ms1_prestataires")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Prestataire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    @Column(nullable = false)
    private String raisonSociale;

    @Column(length = 50)
    private String matriculeFiscal;

    private String adresse;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_utilisateur", nullable = false, unique = true)
    private Utilisateur utilisateur;
}