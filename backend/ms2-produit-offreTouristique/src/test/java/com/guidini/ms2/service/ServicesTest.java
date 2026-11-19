package com.guidini.ms2.service;

import com.guidini.ms2.domain.*;
import com.guidini.ms2.repository.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicesTest {
    private Produit activite() {
        Produit p = new Produit();
        p.setCategorie(CategorieProduit.ACTIVITE);
        p.setTitre("Visite");
        p.setDescription("Visite guidee");
        p.setDestination("Tunis");
        p.setPrixAdulte(new BigDecimal("30.00"));
        p.setCapaciteMax(10);
        p.setDureeMinutes(60);
        return p;
    }

    @Test
    void soumissionPuisAcceptationEtModificationInterdite() {
        ProduitRepository repo = mock(ProduitRepository.class);
        Produit p = activite();
        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(repo.save(any(Produit.class))).thenAnswer(i -> i.getArgument(0));
        ProduitService service = new ProduitService(repo);
        assertThrows(IllegalStateException.class, () -> service.accepterPublication(1L, 9L));
        service.soumettreValidation(1L);
        assertEquals(StatutPublication.EN_ATTENTE, p.getStatut());
        service.accepterPublication(1L, 9L);
        assertEquals(StatutPublication.PUBLIEE, p.getStatut());
        assertEquals(9L, p.getValidatedBy());
        assertThrows(IllegalStateException.class, () -> service.modifierProduit(1L, activite()));
    }

    @Test
    void refusExigeUnMotifEtPermetUneNouvelleSoumission() {
        ProduitRepository repo = mock(ProduitRepository.class);
        Produit p = activite();
        p.setStatut(StatutPublication.EN_ATTENTE);
        when(repo.findById(1L)).thenReturn(Optional.of(p));
        when(repo.save(any(Produit.class))).thenAnswer(i -> i.getArgument(0));
        ProduitService service = new ProduitService(repo);
        assertThrows(IllegalArgumentException.class, () -> service.refuserPublication(1L, 9L, " "));
        service.refuserPublication(1L, 9L, "Description a preciser");
        assertEquals(StatutPublication.REFUSEE, p.getStatut());
        service.soumettreValidation(1L);
        assertNull(p.getMotifRefus());
        assertNull(p.getValidatedBy());
    }

    @Test
    void creationIgnoreIdentiteStatutEtAuditFournis() {
        ProduitRepository repo = mock(ProduitRepository.class);
        when(repo.save(any(Produit.class))).thenAnswer(i -> i.getArgument(0));
        Produit donnees = activite();
        donnees.setIdProduit(50L);
        donnees.setStatut(StatutPublication.PUBLIEE);
        donnees.setValidatedBy(99L);
        Produit cree = new ProduitService(repo).creerProduit(donnees, 2L);
        assertNull(cree.getIdProduit());
        assertNull(cree.getValidatedBy());
        assertEquals(StatutPublication.BROUILLON, cree.getStatut());
        assertEquals(2L, cree.getProprietaireId());
    }

    @Test
    void duplicationCircuitCopieLesEtapesSansLeursIdentifiants() {
        ProduitRepository repo = mock(ProduitRepository.class);
        Produit source = activite();
        source.setCategorie(CategorieProduit.CIRCUIT);
        source.setDureeJours(1);
        EtapeCircuit etape = new EtapeCircuit();
        etape.setIdEtape(7L);
        etape.setNumeroJour(1);
        etape.setOrdre(1);
        etape.setLieu("Medina");
        source.ajouterEtape(etape);
        when(repo.findById(1L)).thenReturn(Optional.of(source));
        when(repo.save(any(Produit.class))).thenAnswer(i -> i.getArgument(0));
        Produit copie = new ProduitService(repo).dupliquerProduit(1L, 2L);
        assertNotSame(etape, copie.getEtapes().get(0));
        assertNull(copie.getEtapes().get(0).getIdEtape());
        assertSame(copie, copie.getEtapes().get(0).getProduit());
        assertSame(source, etape.getProduit());
    }

    @Test
    void validationRejetteCapacitesEtDatesIncoherentes() {
        Produit p = activite();
        p.setCapaciteMin(20);
        assertThrows(IllegalArgumentException.class, () -> ValidationMetier.produit(p, false));
        OffreProduit o = new OffreProduit();
        o.setDateDebut(java.time.LocalDate.of(2026, 6, 10));
        o.setDateFin(java.time.LocalDate.of(2026, 6, 1));
        assertThrows(IllegalArgumentException.class, () -> ValidationMetier.offre(o, false));
    }

    @Test
    void offreNePeutPasPublierUnProduitBrouillon() {
        OffreProduitRepository repo = mock(OffreProduitRepository.class);
        OffreProduit o = new OffreProduit();
        o.setTitre("Offre");
        o.setDescription("Description");
        o.setPrixGlobal(BigDecimal.TEN);
        o.setDateDebut(java.time.LocalDate.of(2026, 6, 1));
        o.setDateFin(java.time.LocalDate.of(2026, 6, 10));
        o.ajouterProduit(activite());
        when(repo.findById(1L)).thenReturn(Optional.of(o));
        OffreProduitService service = new OffreProduitService(repo, mock(ProduitRepository.class));
        assertThrows(IllegalArgumentException.class, () -> service.soumettreValidation(1L));
    }

    @Test
    void etapeNePeutPasEtreAjouteeAUneActivite() {
        ProduitRepository produits = mock(ProduitRepository.class);
        when(produits.findById(1L)).thenReturn(Optional.of(activite()));
        EtapeCircuitRepository etapes = mock(EtapeCircuitRepository.class);
        EtapeCircuitService service = new EtapeCircuitService(etapes, produits);
        assertThrows(IllegalArgumentException.class, () -> service.ajouterEtape(1L, new EtapeCircuit()));
        verify(etapes, never()).save(any());
    }
}
