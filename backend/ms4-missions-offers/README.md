
# MS4 — Missions et offres de prestation

Ce microservice Spring Boot utilise Java 17, Spring Data JPA et H2.

## Structure des packages

| Package | Rôle |
| --- | --- |
| `domain` | Entités JPA et statuts. |
| `exception` | Exceptions métier et gestion des erreurs. |
| `repository` | Accès aux données avec Spring Data JPA. |
| `service` | Règles métier des missions, offres et invitations. |
| `web.controller` | Points d’entrée de l’API REST. |

Les contrôleurs utilisent les entités du domaine pour les échanges avec l’API.

| Entité | Rôle |
| --- | --- |
| `Mission` | Un recruteur indique l’activité MS2, le lieu, les dates, la langue, le type de professionnel et le nombre de places. |
| `OffrePrestation` | Un guide ou un chauffeur publie ses services, son tarif, sa localisation, sa langue et sa période de disponibilité. |
| `Invitation` | Un professionnel peut refuser la mission ou accepter en proposant son tarif. Le recruteur peut ensuite retenir cette réponse. |

Chaque invitation référence une mission. Les offres sont consultables indépendamment des missions. Les identifiants des comptes MS1 et de l’activité MS2 sont stockés comme références numériques. La boîte de réception locale expose les invitations en attendant un contrat d’intégration avec MS7.

## Lancement local

Depuis ce dossier, avec un JDK 17 configuré sous Windows :

```powershell
.\mvnw.cmd spring-boot:run
```

L’application écoute sur le port `8084`. Hibernate crée les tables au démarrage. H2 conserve les données dans `data/ms4-missions-offers.mv.db`, relatif au dossier de lancement.

## API REST

Après le démarrage de MS4, ouvre [Swagger UI](http://localhost:8084/swagger-ui.html). Déplie une route, clique sur **Try it out**, renseigne les paramètres ou le corps JSON, puis clique sur **Execute**. Les exemples ci-dessous permettent de tester le parcours de recrutement.

| Ressource | Créer | Lister | Consulter, modifier ou supprimer |
| --- | --- | --- | --- |
| Missions | `POST /api/missions` | `GET /api/missions` | `GET`, `PUT` ou `DELETE /api/missions/{id}` |
| Offres | `POST /api/offres` | `GET /api/offres` | `GET`, `PUT` ou `DELETE /api/offres/{id}` |
| Invitations | `POST /api/invitations?missionId=1` | `GET /api/invitations` | `GET`, `PUT` ou `DELETE /api/invitations/{id}` |

### Parcours de recrutement

1. Créer un brouillon avec `POST /api/missions`. Exemple de corps JSON.

   ```json
   {"createurId":1,"activiteId":10,"titre":"Visite guidée","description":"Accompagner un groupe","lieu":"Carthage","dateDebut":"2026-12-01","dateFin":"2026-12-02","langue":"français","typePrestation":"GUIDE","nombreProfessionnels":1}
   ```

2. Publier la mission avec `POST /api/missions/{id}/publier`. Une mission ouverte reste modifiable jusqu’à la première invitation.
3. Publier une offre avec `POST /api/offres`. Exemple de corps JSON.

   ```json
   {"prestataireId":2,"services":"Visites guidées","tarifPropose":120,"localisation":"Carthage","langue":"français","typePrestation":"GUIDE","disponibleDu":"2026-12-01","disponibleAu":"2026-12-15"}
   ```

4. Inviter directement avec `POST /api/invitations?missionId=1` et le JSON `{"prestataireId":2,"message":"Disponible ?"}`, ou utiliser `POST /api/invitations/automatiques?missionId=1`. La recherche porte sur les offres publiées avec une localisation et une langue identiques à la casse près, le même type et une disponibilité couvrant toute la mission.
5. Consulter les invitations du professionnel avec `GET /api/invitations/notifications?professionnelId=2`. Répondre avec `POST /api/invitations/{id}/reponse` et le JSON `{"statut":"ACCEPTEE","tarifPropose":145}` ou `{"statut":"REFUSEE"}`.
6. Comparer les réponses avec `GET /api/invitations?missionId=1` et retenir une réponse avec `POST /api/invitations/{id}/selection`. La mission passe à `ATTRIBUEE` quand toutes les places sont pourvues.
7. Suivre la mission avec `GET /api/missions/{id}`. Les actions `POST /api/missions/{id}/terminer` et `POST /api/missions/{id}/annuler` permettent de clore une mission.

Les types de prestation acceptés sont `GUIDE` et `CHAUFFEUR`.
