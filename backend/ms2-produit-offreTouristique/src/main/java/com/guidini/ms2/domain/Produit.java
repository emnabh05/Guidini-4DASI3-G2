package com.guidini.ms2.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "produits")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduit;

    private String titre;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorieProduit categorie;

    private String sousCategorie;

    private String destination;

    private String adresse;

    private String pointRendezVous;

    private String lieuDepart;

    private String lieuArrivee;

    private Integer dureeMinutes;

    private Integer dureeJours;

    private Double distanceKm;

    private String transport;

    private String niveauDifficulte;

    @Column(precision = 12, scale = 2)
    private BigDecimal prixAdulte;

    @Column(precision = 12, scale = 2)
    private BigDecimal prixEnfant;

    private Integer capaciteMin;

    private Integer capaciteMax;

    private Integer ageMinimum;

    private String langues;

    private Boolean accessiblePMR;

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

    @Lob
    private String equipement;

    private String imagePrincipale;

    private String videoUrl;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("numeroJour ASC, ordre ASC")
    private List<EtapeCircuit> etapes = new ArrayList<>();

    public List<EtapeCircuit> getEtapes() {
        return Collections.unmodifiableList(etapes);
    }

    public void setEtapes(List<EtapeCircuit> nouvellesEtapes) {
        for (EtapeCircuit etape : new ArrayList<>(etapes)) {
            supprimerEtape(etape);
        }
        if (nouvellesEtapes != null) {
            // La categorie peut arriver apres les etapes dans le JSON.
            // Le service valide l'ensemble une fois la deserialisation terminee.
            for (EtapeCircuit etape : nouvellesEtapes) {
                if (etape == null) throw new IllegalArgumentException("Etape obligatoire.");
                etapes.add(etape);
                etape.setProduit(this);
            }
        }
    }

    public void ajouterEtape(EtapeCircuit etape) {
        if (categorie != CategorieProduit.CIRCUIT) {
            throw new IllegalStateException("Seul un circuit peut contenir des etapes.");
        }
        if (etape == null) {
            throw new IllegalArgumentException("L'etape est obligatoire.");
        }
        if (etape.getProduit() != null && etape.getProduit() != this) {
            throw new IllegalArgumentException("Cette etape appartient deja a un autre produit.");
        }
        if (!etapes.contains(etape)) {
            etapes.add(etape);
            etape.setProduit(this);
        }
    }

    public void supprimerEtape(EtapeCircuit etape) {
        if (etapes.remove(etape)) {
            etape.setProduit(null);
        }
    }

    public Produit() {
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
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

    public CategorieProduit getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieProduit categorie) {
        if (categorie != CategorieProduit.CIRCUIT && !etapes.isEmpty()) {
            throw new IllegalStateException("Retirer les etapes avant de changer la categorie.");
        }
        this.categorie = categorie;
    }

    public String getSousCategorie() {
        return sousCategorie;
    }

    public void setSousCategorie(String sousCategorie) {
        this.sousCategorie = sousCategorie;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPointRendezVous() {
        return pointRendezVous;
    }

    public void setPointRendezVous(String pointRendezVous) {
        this.pointRendezVous = pointRendezVous;
    }

    public String getLieuDepart() {
        return lieuDepart;
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    public String getLieuArrivee() {
        return lieuArrivee;
    }

    public void setLieuArrivee(String lieuArrivee) {
        this.lieuArrivee = lieuArrivee;
    }

    public Integer getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(Integer dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public Integer getDureeJours() {
        return dureeJours;
    }

    public void setDureeJours(Integer dureeJours) {
        this.dureeJours = dureeJours;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getNiveauDifficulte() {
        return niveauDifficulte;
    }

    public void setNiveauDifficulte(String niveauDifficulte) {
        this.niveauDifficulte = niveauDifficulte;
    }

    public BigDecimal getPrixAdulte() {
        return prixAdulte;
    }

    public void setPrixAdulte(BigDecimal prixAdulte) {
        this.prixAdulte = prixAdulte;
    }

    public BigDecimal getPrixEnfant() {
        return prixEnfant;
    }

    public void setPrixEnfant(BigDecimal prixEnfant) {
        this.prixEnfant = prixEnfant;
    }

    public Integer getCapaciteMin() {
        return capaciteMin;
    }

    public void setCapaciteMin(Integer capaciteMin) {
        this.capaciteMin = capaciteMin;
    }

    public Integer getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(Integer capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public Integer getAgeMinimum() {
        return ageMinimum;
    }

    public void setAgeMinimum(Integer ageMinimum) {
        this.ageMinimum = ageMinimum;
    }

    public String getLangues() {
        return langues;
    }

    public void setLangues(String langues) {
        this.langues = langues;
    }

    public Boolean getAccessiblePMR() {
        return accessiblePMR;
    }

    public void setAccessiblePMR(Boolean accessiblePMR) {
        this.accessiblePMR = accessiblePMR;
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

    public String getEquipement() {
        return equipement;
    }

    public void setEquipement(String equipement) {
        this.equipement = equipement;
    }

    public String getImagePrincipale() {
        return imagePrincipale;
    }

    public void setImagePrincipale(String imagePrincipale) {
        this.imagePrincipale = imagePrincipale;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

