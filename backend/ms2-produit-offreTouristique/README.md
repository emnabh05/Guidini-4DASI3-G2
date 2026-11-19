# Guidini — MS2

Backend Spring Boot, Java 17 et H2. API sans DTO : les requêtes et réponses utilisent
les entités. Les champs d'identité, d'audit et de statut fournis lors d'une création
ou modification ne remplacent pas ceux contrôlés par les services.

## Routes REST

Base locale : `http://localhost:8082`.
Dans le tableau, `{ressource}` vaut `produits` ou `offres`.

| Méthode | Route | Utilisation |
|---|---|---|
| GET | /api/{ressource} | Catalogue publié, public |
| GET | /api/{ressource}/{id} | Détail publié, public |
| GET | /api/{ressource}/mes-publications | Publications de l'utilisateur connecté |
| GET | /api/{ressource}/{id}/gestion | Détail pour le propriétaire ou l'administrateur |
| GET | /api/{ressource}/demandes-publication | Demandes en attente, administrateur |
| POST | /api/{ressource} | Création en brouillon, réponse 201 et Location |
| PUT | /api/{ressource}/{id} | Remplacement des champs éditables |
| POST | /api/{ressource}/{id}/dupliquer | Duplication en brouillon |
| PATCH | /api/{ressource}/{id}/archiver | Archivage |
| POST | /api/{ressource}/{id}/soumettre | Soumission à validation |
| POST | /api/{ressource}/{id}/accepter | Acceptation, administrateur |
| POST | /api/{ressource}/{id}/refuser?motif=... | Refus motivé, administrateur |
| POST | /api/{ressource}/{id}/publier | Publication directe d'une création de l'administrateur |
| GET | /api/produits/recherche?destination=Tunis | Recherche publique |
| GET | /api/produits/categorie/CIRCUIT | Filtre public par catégorie |
| POST | /api/offres/{id}/produits/{idProduit} | Ajout d'un produit à une offre |
| DELETE | /api/offres/{id}/produits/{idProduit} | Retrait du lien, sans supprimer le produit |
| GET | /api/produits/{idProduit}/etapes | Étapes d'un produit publié |
| POST | /api/produits/{idProduit}/etapes | Ajout d'une étape |
| GET | /api/etapes/{idEtape} | Détail public si le produit est publié, sinon accès contrôlé |
| PUT | /api/etapes/{idEtape} | Modification d'une étape |
| PATCH | /api/etapes/{idEtape}/ordre?numeroJour=1&ordre=2 | Changement de position |
| DELETE | /api/etapes/{idEtape} | Suppression d'une étape, réponse 204 |

Les relations se modifient via leurs routes dédiées. PUT sur un produit ou une offre
modifie ses champs simples, sans remplacer ses étapes ou produits.
Les erreurs sont renvoyées au format ProblemDetail : 400 pour les données invalides,
401 sans authentification, 403 sans autorisation, 404 si absent et 409 si le statut
empêche l'opération.

## Authentification à raccorder

JWT n'est pas encore implémenté. Les routes de gestion renvoient 401 tant qu'aucun
principal authentifié n'est fourni par le serveur. Envoyer un header d'identifiant
ou un jeton non traité ne permet pas de se connecter.

Le contrat provisoire centralisé dans AccesUtilisateur attend un principal dont
le nom est l'identifiant numérique MS1 et les rôles ADMIN ou PRESTATAIRE.
Ce contrat devra être adapté aux claims et rôles réels de MS1 lors du branchement
de Spring Security + JWT. Les contrôleurs ne vérifient pas eux-mêmes de signature JWT.

## Données et tests

H2 utilise le fichier `./data/guidini-ms2`, relatif au répertoire de lancement.
Les tests HTTP utilisent une base H2 en mémoire distincte et simulent le principal
authentifié : ils ne constituent pas des tests de validation JWT.

Les tests couvrent les services, les réponses HTTP, les permissions, la
sérialisation JSON des relations et la suppression persistée des étapes.
