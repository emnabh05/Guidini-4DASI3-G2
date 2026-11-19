package com.guideme.ms4missionsoffers.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long createurId;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String lieu;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutMission statut = StatutMission.BROUILLON;

    private Long activiteId;
    private String langue;

    @Enumerated(EnumType.STRING)
    private TypePrestation typePrestation;

    private Integer nombreProfessionnels;

    protected Mission() {
    }

    public Mission(Long createurId, String titre, String description, String lieu, LocalDate dateDebut, LocalDate dateFin) {
        this.createurId = createurId;
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public void modifier(String titre, String description, String lieu, LocalDate dateDebut, LocalDate dateFin) {
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public void definirBesoins(Mission saisie) {
        this.activiteId = saisie.activiteId;
        this.langue = saisie.langue.strip();
        this.typePrestation = saisie.typePrestation;
        this.nombreProfessionnels = saisie.nombreProfessionnels;
    }

    public void changerStatut(StatutMission statut) {
        this.statut = statut;
    }

    public Long getActiviteId() {
        return activiteId;
    }

    public String getLangue() {
        return langue;
    }

    public TypePrestation getTypePrestation() {
        return typePrestation;
    }

    public Integer getNombreProfessionnels() {
        return nombreProfessionnels;
    }

    public Long getId() {
        return id;
    }

    public Long getCreateurId() {
        return createurId;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getLieu() {
        return lieu;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public StatutMission getStatut() {
        return statut;
    }
}
