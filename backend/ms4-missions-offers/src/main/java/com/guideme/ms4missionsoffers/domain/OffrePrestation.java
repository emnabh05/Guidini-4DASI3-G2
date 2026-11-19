package com.guideme.ms4missionsoffers.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offres_prestation")
public class OffrePrestation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long prestataireId;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal tarifPropose;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutOffre statut = StatutOffre.PUBLIEE;

    @Column(length = 2000)
    private String services;
    private String localisation;
    private String langue;

    @Enumerated(EnumType.STRING)
    private TypePrestation typePrestation;

    private LocalDate disponibleDu;
    private LocalDate disponibleAu;

    protected OffrePrestation() {
    }

    public OffrePrestation(Long prestataireId, BigDecimal tarifPropose, String message) {
        this.prestataireId = prestataireId;
        this.tarifPropose = tarifPropose;
        this.message = message;
    }

    public void modifier(BigDecimal tarifPropose, String message) {
        this.tarifPropose = tarifPropose;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void definirDisponibilite(OffrePrestation saisie) {
        this.services = saisie.services.strip();
        this.localisation = saisie.localisation.strip();
        this.langue = saisie.langue.strip();
        this.typePrestation = saisie.typePrestation;
        this.disponibleDu = saisie.disponibleDu;
        this.disponibleAu = saisie.disponibleAu;
        this.statut = StatutOffre.PUBLIEE;
    }

    public String getServices() {
        return services;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getLangue() {
        return langue;
    }

    public TypePrestation getTypePrestation() {
        return typePrestation;
    }

    public LocalDate getDisponibleDu() {
        return disponibleDu;
    }

    public LocalDate getDisponibleAu() {
        return disponibleAu;
    }

    public Long getPrestataireId() {
        return prestataireId;
    }

    public BigDecimal getTarifPropose() {
        return tarifPropose;
    }

    public String getMessage() {
        return message;
    }

    public StatutOffre getStatut() {
        return statut;
    }
}
