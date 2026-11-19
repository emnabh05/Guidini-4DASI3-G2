package com.guideme.ms1users.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ms1_documents_professionnels")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DocumentProfessionnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocument;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDocument typeDocument;

    private String numeroDocument;

    private LocalDate dateExpiration;

    @Column(nullable = false)
    private String cheminFichier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVerification statutVerification;

    @Column(length = 500)
    private String motifRefus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateDepot;

    private LocalDateTime dateVerification;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

    @PrePersist
    void onCreate() {
        dateDepot = LocalDateTime.now();
        if (statutVerification == null) statutVerification = StatutVerification.EN_ATTENTE;
    }
}