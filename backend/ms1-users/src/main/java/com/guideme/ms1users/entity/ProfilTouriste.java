package com.guideme.ms1users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ms1_profils_touristes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProfilTouriste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfil;

    private String preferences;
    private String pays;
    private String langue;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_utilisateur", nullable = false, unique = true)
    private Utilisateur utilisateur;
}