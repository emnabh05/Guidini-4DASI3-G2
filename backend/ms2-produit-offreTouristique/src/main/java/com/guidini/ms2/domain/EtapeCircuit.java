package com.guidini.ms2.domain;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "etapes_circuit")
public class EtapeCircuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtape;

    private Integer numeroJour;

    private Integer ordre;

    private String lieu;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    private Double distanceKm;

    @Lob
    private String description;

    // idProduit est represente par la cle etrangere de cette relation.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_produit", nullable = false)
    private Produit produit;

    @com.fasterxml.jackson.annotation.JsonIgnore
    public Produit getProduit() {
        return produit;
    }

    // Utiliser Produit.ajouterEtape / supprimerEtape pour synchroniser la relation.
    void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Long getIdProduit() {
        return produit == null ? null : produit.getIdProduit();
    }

    public EtapeCircuit() {
    }

    public Long getIdEtape() {
        return idEtape;
    }

    public void setIdEtape(Long idEtape) {
        this.idEtape = idEtape;
    }

    public Integer getNumeroJour() {
        return numeroJour;
    }

    public void setNumeroJour(Integer numeroJour) {
        this.numeroJour = numeroJour;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

