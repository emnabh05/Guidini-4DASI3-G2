package com.guidini.ms2.service;

import com.guidini.ms2.domain.*;
import java.math.BigDecimal;

final class ValidationMetier {
    private ValidationMetier() {}

    static void exiger(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    static void texte(String valeur, String champ) {
        exiger(valeur != null && !valeur.isBlank(), champ + " est obligatoire.");
    }

    static void identifiant(Long id) {
        exiger(id != null && id > 0, "Identifiant utilisateur invalide.");
    }

    static void modifiable(StatutPublication statut) {
        if (statut != StatutPublication.BROUILLON && statut != StatutPublication.REFUSEE) {
            throw new IllegalStateException("Seuls les brouillons et elements refuses sont modifiables.");
        }
    }

    static void enAttente(StatutPublication statut) {
        if (statut != StatutPublication.EN_ATTENTE) {
            throw new IllegalStateException("La publication doit etre en attente.");
        }
    }

    static void prix(BigDecimal valeur) {
        exiger(valeur == null || (valeur.signum() >= 0
                && valeur.stripTrailingZeros().scale() <= 2
                && valeur.compareTo(new BigDecimal("9999999999.99")) <= 0),
                "Le prix doit etre positif ou nul, avec au plus deux decimales.");
    }

    static void positif(Integer valeur, String champ) {
        exiger(valeur == null || valeur > 0, champ + " doit etre positif.");
    }

    static void produit(Produit p, boolean publication) {
        exiger(p != null, "Produit obligatoire.");
        exiger(p.getCategorie() != null, "Categorie obligatoire.");
        prix(p.getPrixAdulte());
        prix(p.getPrixEnfant());
        positif(p.getDureeMinutes(), "Duree en minutes");
        positif(p.getDureeJours(), "Duree en jours");
        positif(p.getCapaciteMin(), "Capacite minimale");
        positif(p.getCapaciteMax(), "Capacite maximale");
        exiger(p.getCapaciteMin() == null || p.getCapaciteMax() == null
                || p.getCapaciteMin() <= p.getCapaciteMax(), "Capacites incoherentes.");
        exiger(p.getAgeMinimum() == null || p.getAgeMinimum() >= 0, "Age minimum invalide.");
        distance(p.getDistanceKm());
        if (publication) {
            texte(p.getTitre(), "Titre");
            texte(p.getDescription(), "Description");
            texte(p.getDestination(), "Destination");
            exiger(p.getPrixAdulte() != null && p.getCapaciteMax() != null,
                    "Prix adulte et capacite maximale obligatoires.");
            if (p.getCategorie() == CategorieProduit.CIRCUIT) {
                exiger(p.getDureeJours() != null && !p.getEtapes().isEmpty(),
                        "Un circuit doit avoir une duree en jours et des etapes.");
            } else {
                exiger(p.getDureeMinutes() != null, "Duree en minutes obligatoire.");
            }
        }
        java.util.Set<String> positions = new java.util.HashSet<>();
        for (EtapeCircuit e : p.getEtapes()) {
            etape(e, p);
            exiger(positions.add(e.getNumeroJour() + ":" + e.getOrdre()),
                    "Deux etapes ne peuvent pas occuper la meme position le meme jour.");
        }
    }

    static void offre(OffreProduit o, boolean publication) {
        exiger(o != null, "Offre obligatoire.");
        prix(o.getPrixGlobal());
        exiger(o.getDateDebut() == null || o.getDateFin() == null
                || !o.getDateFin().isBefore(o.getDateDebut()), "Dates de validite incoherentes.");
        if (publication) {
            texte(o.getTitre(), "Titre");
            texte(o.getDescription(), "Description");
            exiger(o.getPrixGlobal() != null && o.getDateDebut() != null && o.getDateFin() != null,
                    "Prix global et dates obligatoires.");
            exiger(!o.getProduits().isEmpty(), "Une offre doit contenir au moins un produit.");
            exiger(o.getProduits().stream().allMatch(p -> p.getStatut() == StatutPublication.PUBLIEE),
                    "Tous les produits de l'offre doivent etre publies.");
        }
    }

    static void distance(Double valeur) {
        exiger(valeur == null || (Double.isFinite(valeur) && valeur >= 0), "Distance invalide.");
    }

    static void etape(EtapeCircuit e, Produit p) {
        exiger(e != null, "Etape obligatoire.");
        exiger(p.getCategorie() == CategorieProduit.CIRCUIT, "Le produit doit etre un circuit.");
        exiger(e.getNumeroJour() != null && e.getNumeroJour() > 0, "Numero de jour invalide.");
        exiger(e.getOrdre() != null && e.getOrdre() > 0, "Ordre invalide.");
        exiger(p.getDureeJours() == null || e.getNumeroJour() <= p.getDureeJours(),
                "Le jour depasse la duree du circuit.");
        texte(e.getLieu(), "Lieu");
        distance(e.getDistanceKm());
        exiger(e.getHeureDebut() == null || e.getHeureFin() == null
                || e.getHeureFin().isAfter(e.getHeureDebut()), "Horaires incoherents.");
    }
}

