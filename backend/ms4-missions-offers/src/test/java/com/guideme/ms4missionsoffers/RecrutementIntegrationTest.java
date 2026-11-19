package com.guideme.ms4missionsoffers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:ms4-integration",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class RecrutementIntegrationTest {
    @LocalServerPort
    private int port;

    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void publieUneMissionEtRetientUnProfessionnelCompatible() throws Exception {
        int mission = creerMission();
        verifier(appeler("POST", "/missions/" + mission + "/publier", null), 200, "$.statut", "OUVERTE");

        publierOffre(21, "GUIDE", "2026-12-10");
        publierOffre(22, "CHAUFFEUR", "2026-12-10");
        publierOffre(23, "GUIDE", "2026-12-01");

        String recherche = "/invitations/automatiques?missionId=" + mission;
        String invitations = corps(appeler("POST", recherche, null), 201);
        List<Integer> professionnels = JsonPath.read(invitations, "$[*].prestataireId");
        assertThat(professionnels).containsExactly(21);
        int invitation = JsonPath.read(invitations, "$[0].id");
        assertThat(corps(appeler("POST", recherche, null), 201)).isEqualTo("[]");

        String boite = corps(appeler("GET", "/invitations/notifications?professionnelId=21", null), 200);
        assertThat(JsonPath.parse(boite).read("$[0].notifieeLe", String.class)).isNotBlank();

        String route = "/invitations/" + invitation;
        corps(appeler("POST", route + "/selection", null), 409);
        String acceptation = corps(appeler("POST", route + "/reponse", """
                {"statut":"ACCEPTEE","tarifPropose":145}
                """), 200);
        Number tarif = JsonPath.read(acceptation, "$.tarifPropose");
        assertThat(tarif.doubleValue()).isEqualTo(145.0);
        verifier(appeler("POST", route + "/selection", null), 200, "$.statut", "RETENUE");
        verifier(appeler("GET", "/missions/" + mission, null), 200, "$.statut", "ATTRIBUEE");
        verifier(appeler("POST", "/missions/" + mission + "/terminer", null), 200, "$.statut", "TERMINEE");
    }

    @Test
    void refuseUneInvitationEtAnnuleLaMission() throws Exception {
        int mission = creerMission();
        String creation = "/invitations?missionId=" + mission;
        String destinataire = """
                {"prestataireId":42}
                """;
        corps(appeler("POST", creation, destinataire), 409);
        corps(appeler("POST", "/missions/" + mission + "/publier", null), 200);

        int invitation = JsonPath.read(corps(appeler("POST", creation, destinataire), 201), "$.id");
        corps(appeler("POST", creation, destinataire), 409);
        String reponse = "/invitations/" + invitation + "/reponse";
        corps(appeler("POST", reponse, """
                {"statut":"ACCEPTEE","tarifPropose":-1}
                """), 400);
        verifier(appeler("POST", reponse, """
                {"statut":"REFUSEE"}
                """), 200, "$.statut", "REFUSEE");
        corps(appeler("POST", "/invitations/" + invitation + "/selection", null), 409);
        verifier(appeler("POST", "/missions/" + mission + "/annuler", null), 200, "$.statut", "ANNULEE");
        corps(appeler("POST", creation, """
                {"prestataireId":43}
                """), 409);
    }

    @Test
    void rejetteUneMissionSansActiviteEtSignaleUneMissionAbsente() throws Exception {
        corps(appeler("POST", "/missions", """
                {"createurId":1,"titre":"Sans activité"}
                """), 400);
        corps(appeler("GET", "/missions/999999", null), 404);
    }

    private int creerMission() throws Exception {
        String mission = corps(appeler("POST", "/missions", """
                {"createurId":1,"activiteId":10,"titre":"Visite","description":"Un groupe",
                 "lieu":"Carthage","dateDebut":"2026-12-01","dateFin":"2026-12-02",
                 "langue":"fr","typePrestation":"GUIDE","nombreProfessionnels":1}
                """), 201);
        assertThat(JsonPath.parse(mission).read("$.statut", String.class)).isEqualTo("BROUILLON");
        return JsonPath.read(mission, "$.id");
    }

    private void publierOffre(int professionnel, String type, String disponibleAu) throws Exception {
        verifier(appeler("POST", "/offres", """
                {"prestataireId":%d,"services":"Visites","tarifPropose":120,"localisation":"carthage",
                 "langue":"FR","typePrestation":"%s","disponibleDu":"2026-11-30","disponibleAu":"%s"}
                """.formatted(professionnel, type, disponibleAu)), 201, "$.statut", "PUBLIEE");
    }

    private HttpResponse<String> appeler(String methode, String chemin, String json) throws Exception {
        HttpRequest requete = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api" + chemin))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .method(methode, json == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(requete, HttpResponse.BodyHandlers.ofString());
    }

    private String corps(HttpResponse<String> reponse, int statut) {
        assertThat(reponse.statusCode()).as("Réponse HTTP : %s", reponse.body()).isEqualTo(statut);
        return reponse.body();
    }

    private void verifier(HttpResponse<String> reponse, int statut, String champ, Object attendu) {
        Object obtenu = JsonPath.read(corps(reponse, statut), champ);
        assertThat(obtenu).isEqualTo(attendu);
    }
}
