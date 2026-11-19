# MS3 - Réservations et Paiements (Guidini)

Microservice Spring Boot qui gère les **réservations** d'activités ou d'offres touristiques et leurs **paiements**.
Il appartient à la plateforme Guidini (architecture microservices) et possède sa propre base **MySQL**.

## Technologies

| Rôle | Technologie |
|---|---|
| Framework | Spring Boot 4.1.2, Java 17 |
| Base de données | MySQL 8 (base `ms3_db`) |
| Messagerie asynchrone | RabbitMQ |
| Documentation de l'API | Swagger (springdoc-openapi) |
| Conteneurisation | Docker, Docker Compose |
| Tests | JUnit 5, Mockito |

## Lancer le service

### Avec Docker (recommandé)

Prérequis : Docker Desktop ouvert.

```
docker compose up -d --build
```

Cette commande démarre MySQL, RabbitMQ et le service. Pour arrêter : `docker compose down`.

### En développement (service lancé depuis Maven)

```
docker compose up -d ms3-db rabbitmq
./mvnw spring-boot:run
```

### Adresses utiles

| Quoi | Adresse |
|---|---|
| Swagger UI | http://localhost:8083/swagger-ui.html |
| API | http://localhost:8083/api |
| Interface RabbitMQ | http://localhost:15672 (guest / guest) |
| MySQL | localhost:3307 (root / root, base `ms3_db`) |

## Endpoints

### Réservations

| Méthode | Route | Description | Codes |
|---|---|---|---|
| POST | /api/reservations | Créer une réservation | 201, 400 |
| GET | /api/reservations | Lister (filtre optionnel `?statut=CONFIRMEE`) | 200, 400 |
| GET | /api/reservations/{id} | Consulter une réservation | 200, 404 |
| GET | /api/reservations/client/{idClient} | Historique d'un client | 200, 404 |
| PUT | /api/reservations/{id}/participants | Modifier les participants | 200, 400, 404, 409 |
| PATCH | /api/reservations/{id}/annulation | Annuler (avec remboursement) | 200, 404, 409 |
| DELETE | /api/reservations/{id} | Supprimer (ANNULEE ou EXPIREE uniquement) | 204, 404, 409 |

### Paiements

| Méthode | Route | Description | Codes |
|---|---|---|---|
| POST | /api/paiements | Payer une réservation | 201, 400, 404, 409 |
| GET | /api/paiements/{id} | Consulter un paiement | 200, 404 |
| GET | /api/paiements/reservation/{idReservation} | Paiements d'une réservation | 200, 404 |

Format des erreurs :

```json
{
  "timestamp": "2026-11-19T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Réservation 999 introuvable",
  "path": "/api/reservations/999"
}
```

## Règles métier

- **Montant** : prix unitaire x nombre de places. Exemple : 3 places à 50 TND = **150 TND**.
- **Participants** : leur nombre doit être égal au nombre de places (sinon 400).
- **Statuts d'une réservation** : `EN_ATTENTE_PAIEMENT`, `CONFIRMEE`, `ANNULEE`, `EXPIREE`.
- **Paiement** : le montant est pris dans la réservation (jamais dans la requête). Un paiement valide passe la réservation à `CONFIRMEE`. Un second paiement est refusé (409).
- **Expiration** : une réservation non payée au bout de **15 minutes** passe à `EXPIREE` (tâche planifiée toutes les minutes).
- **Remboursement** à l'annulation, calculé depuis la date de réservation :

| Situation | Remboursement | Exemple (150 TND) |
|---|---|---|
| Non payée | 0 % | 0 TND |
| Payée, annulée dans les 24 h | 100 % | 150 TND |
| Payée, annulée après 24 h | 50 % | 75 TND |

## Communication entre services

### Asynchrone (RabbitMQ)

Quand un paiement est validé, le service publie l'événement « réservation confirmée ».

| Élément | Valeur |
|---|---|
| Exchange (topic) | `reservation.exchange` |
| Routing key | `reservation.confirmee` |
| Queue | `reservation.confirmee.queue` |

```json
{"idReservation":2,"idClient":1,"nombrePlaces":3,"montantTotal":150.00,"dateConfirmation":"2026-11-19T20:59:37.2823787"}
```

Si RabbitMQ est indisponible, le paiement n'échoue pas : l'erreur est seulement journalisée.

## Tests

```
./mvnw test -Dtest=ReservationServiceTest,PaiementServiceTest
```

14 tests unitaires sur les règles métier (montants, remboursements, expiration, conflits, publication de l'événement).

## Structure du code

```
src/main/java/com/guideme/ms3_reservations_payments/
  config/       RabbitMQ et planification
  controller/   endpoints REST
  dto/          objets de requête et de réponse
  entity/       Reservation, Paiement
  enums/        TypeReservation, StatutReservation, StatutPaiement, ModePaiement
  exception/    exceptions métier et gestion globale des erreurs
  messaging/    événement et publication RabbitMQ
  repository/   accès à MySQL (Spring Data JPA)
  service/      logique métier
```

## État d'avancement

Fait : entités, API REST, paiements, annulation et remboursement, expiration, RabbitMQ, Swagger, tests, Docker.

À venir (travail commun du groupe) : Config Server, Eureka, appel synchrone de MS2 (prix et disponibilité, aujourd'hui prix provisoire de 50 TND par place), API Gateway, Keycloak.