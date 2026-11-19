package com.guidini.ms2.web.controller;

import com.guidini.ms2.domain.*;
import com.guidini.ms2.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:controleurs;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=false"
})
class ControleursTest {
    @Autowired WebApplicationContext context;
    @Autowired ProduitRepository produits;
    @Autowired OffreProduitRepository offres;
    private MockMvc mvc;

    @BeforeEach
    void preparer() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        offres.deleteAll();
        produits.deleteAll();
    }

    private RequestPostProcessor acteur(long id, String role) {
        return request -> {
            request.setUserPrincipal(() -> Long.toString(id));
            request.addUserRole(role);
            return request;
        };
    }

    private Produit produit(StatutPublication statut) {
        Produit p = new Produit();
        p.setTitre("Circuit Tunis");
        p.setCategorie(CategorieProduit.CIRCUIT);
        p.setProprietaireId(1L);
        p.setStatut(statut);
        EtapeCircuit e = new EtapeCircuit();
        e.setNumeroJour(1);
        e.setOrdre(1);
        e.setLieu("Medina");
        p.ajouterEtape(e);
        return produits.save(p);
    }

    @Test
    void cataloguePublicSansBrouillonsEtSansBoucleJson() throws Exception {
        produit(StatutPublication.BROUILLON);
        Produit publie = produit(StatutPublication.PUBLIEE);
        mvc.perform(get("/api/produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].etapes[0].lieu").value("Medina"))
                .andExpect(jsonPath("$[0].etapes[0].produit").doesNotExist());
        mvc.perform(get("/api/produits/" + publie.getIdProduit()))
                .andExpect(status().isOk());
    }

    @Test
    void anonymeNePeutPasCreerEtBrouillonNonPublic() throws Exception {
        mvc.perform(post("/api/produits").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categorie\":\"ACTIVITE\"}"))
                .andExpect(status().isUnauthorized());
        Produit p = produit(StatutPublication.BROUILLON);
        mvc.perform(get("/api/produits/" + p.getIdProduit())).andExpect(status().isNotFound());
    }

    @Test
    void creationCircuitAvecEtapesAvantCategorieEtLocationValide() throws Exception {
        String location = mvc.perform(post("/api/produits").with(acteur(1, "PRESTATAIRE"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"etapes":[{"numeroJour":1,"ordre":1,"lieu":"Medina"}],
                             "categorie":"CIRCUIT","titre":"Circuit","statut":"PUBLIEE"}
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").value("BROUILLON"))
                .andExpect(jsonPath("$.etapes[0].idEtape").isNumber())
                .andReturn().getResponse().getHeader("Location");
        mvc.perform(get(location).with(acteur(1, "PRESTATAIRE"))).andExpect(status().isOk());
    }

    @Test
    void autreProprietaireEtPrestataireNePeuventPasValider() throws Exception {
        Produit p = produit(StatutPublication.BROUILLON);
        mvc.perform(patch("/api/produits/" + p.getIdProduit() + "/archiver")
                        .with(acteur(2, "PRESTATAIRE"))).andExpect(status().isForbidden());
        mvc.perform(post("/api/produits/" + p.getIdProduit() + "/accepter")
                        .with(acteur(1, "PRESTATAIRE"))).andExpect(status().isForbidden());
    }

    @Test
    void erreursMetierEtJsonOntLesCodesHttpAttendus() throws Exception {
        mvc.perform(post("/api/produits").with(acteur(1, "PRESTATAIRE"))
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/produits").with(acteur(1, "PRESTATAIRE"))
                        .contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
        Produit p = produit(StatutPublication.PUBLIEE);
        mvc.perform(put("/api/produits/" + p.getIdProduit()).with(acteur(1, "PRESTATAIRE"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"categorie\":\"ACTIVITE\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void offreChargeProduitsEtEtapesSansSessionOuvertePendantJson() throws Exception {
        Produit p = produit(StatutPublication.PUBLIEE);
        mvc.perform(post("/api/offres").with(acteur(1, "PRESTATAIRE"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titre\":\"Pack\",\"produits\":[{\"idProduit\":" + p.getIdProduit() + "}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.produits[0].etapes[0].lieu").value("Medina"));
    }

    @Test
    void suppressionEtapePersisteEtRetourne204() throws Exception {
        Produit p = produit(StatutPublication.BROUILLON);
        Long idEtape = p.getEtapes().get(0).getIdEtape();
        mvc.perform(delete("/api/etapes/" + idEtape).with(acteur(1, "PRESTATAIRE")))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/etapes/" + idEtape)).andExpect(status().isNotFound());
    }
}
