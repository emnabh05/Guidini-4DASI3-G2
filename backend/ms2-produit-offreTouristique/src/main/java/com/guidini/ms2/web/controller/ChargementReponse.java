package com.guidini.ms2.web.controller;

import com.guidini.ms2.domain.*;
import java.util.List;

/** Charge les relations dans la transaction avant la serialisation JSON sans DTO. */
final class ChargementReponse {
    private ChargementReponse() {}

    static Produit produit(Produit produit) {
        produit.getEtapes().size();
        return produit;
    }

    static OffreProduit offre(OffreProduit offre) {
        offre.getProduits().forEach(ChargementReponse::produit);
        return offre;
    }

    static List<Produit> produits(List<Produit> produits) {
        produits.forEach(ChargementReponse::produit);
        return produits;
    }

    static List<OffreProduit> offres(List<OffreProduit> offres) {
        offres.forEach(ChargementReponse::offre);
        return offres;
    }
}

