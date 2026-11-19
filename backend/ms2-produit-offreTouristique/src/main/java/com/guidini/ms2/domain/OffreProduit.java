package com.guidini.ms2.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;

@Entity
@Table(name = "offres_produits")
public class OffreProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOffre;

    private String titre;

    @Lob
    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal prixGlobal;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Lob
    private String inclus;

    @Lob
    private String nonInclus;

    @Lob
    private String conditions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPublication statut = StatutPublication.BROUILLON;

    private Long createdBy;

    private Long proprietaireId;

    private Long validatedBy;

    @Lob
    private String motifRefus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Les produits ont leur propre cycle de vie : aucune suppression en cascade.
    @ManyToMany
    @JoinTable(name = "offre_produits",
        joinColumns = @JoinColumn(name = "offre_id"),
        inverseJoinColumns = @JoinColumn(name = "produit_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"offre_id", "produit_id"}))
    private List<Produit> produits = new ArrayList<>();

    public List<Produit> getProduits() {
        return Collections.unmodifiableList(produits);
    }

    public void setProduits(List<Produit> nouveauxProduits) {
        produits.clear();
        if (nouveauxProduits != null) {
            nouveauxProduits.forEach(this::ajouterProduit);
        }
    }

    public void ajouterProduit(Produit produit) {
        if (produit == null) {
            throw new IllegalArgumentException("Le produit est obligatoire.");
        }
        if (!produits.contains(produit)) {
            produits.add(produit);
        }
    }

    public void retirerProduit(Produit produit) {
        produits.remove(produit);
    }

    public OffreProduit() {
    }

    public Long getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(Long idOffre) {
        this.idOffre = idOffre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrixGlobal() {
        return prixGlobal;
    }

    public void setPrixGlobal(BigDecimal prixGlobal) {
        this.prixGlobal = prixGlobal;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getInclus() {
        return inclus;
    }

    public void setInclus(String inclus) {
        this.inclus = inclus;
    }

    public String getNonInclus() {
        return nonInclus;
    }

    public void setNonInclus(String nonInclus) {
        this.nonInclus = nonInclus;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public StatutPublication getStatut() {
        return statut;
    }

    public void setStatut(StatutPublication statut) {
        this.statut = statut;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(Long proprietaireId) {
        this.proprietaireId = proprietaireId;
    }

    public Long getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(Long validatedBy) {
        this.validatedBy = validatedBy;
    }

    public String getMotifRefus() {
        return motifRefus;
    }

    public void setMotifRefus(String motifRefus) {
        this.motifRefus = motifRefus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void avantCreation() {
        LocalDateTime maintenant = LocalDateTime.now();
        createdAt = maintenant;
        updatedAt = maintenant;
    }

    @PreUpdate
    protected void avantModification() {
        updatedAt = LocalDateTime.now();
    }
}

