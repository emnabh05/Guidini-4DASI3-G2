# 🌍 Guidini – Plateforme de réservation d'activités touristiques en Tunisie

<p align="center">
  <strong>Découvrez la Tunisie autrement !</strong><br/>
  Explorez, partagez et réservez vos expériences touristiques.
</p>

---

## 📌 1. Présentation du projet

**GuideMe** est une plateforme web dédiée à la découverte, à la promotion et à la réservation d'activités touristiques en Tunisie.

Elle permet aux touristes de découvrir des expériences variées, de consulter les activités et les offres proposées par les professionnels du secteur, puis d'effectuer leurs réservations en ligne.

La plateforme propose également des fonctionnalités complémentaires telles que le paiement, les recommandations intelligentes basées sur l'intelligence artificielle, le partage de publications, la gestion des réclamations et les notifications.

GuideMe vise ainsi à créer un environnement numérique qui facilite les échanges entre les touristes et les professionnels du tourisme, tout en valorisant la richesse des expériences touristiques tunisiennes.

Le projet repose sur une **architecture microservices**, permettant de répartir les différentes fonctionnalités en services indépendants, organisés autour de domaines métier spécifiques.

## 🎯 2. Objectifs du projet

Les principaux objectifs de GuideMe sont :

- Faciliter la découverte des destinations et des activités touristiques en Tunisie.
- Centraliser les produits, les circuits et les offres touristiques.
- Simplifier le processus de réservation des activités.
- Assurer la gestion des paiements et des remboursements.
- Mettre en relation les touristes et les professionnels du tourisme.
- Proposer des recommandations personnalisées grâce à l'intelligence artificielle.
- Favoriser les échanges entre utilisateurs à travers un espace communautaire.
- Gérer les réclamations et les conflits entre les différents acteurs.
- Améliorer l'expérience utilisateur grâce à un système de notifications.

## 🏗️ 3. Architecture du projet

GuideMe adopte une architecture basée sur les microservices. Chaque microservice est responsable d'un domaine fonctionnel précis et possède sa propre logique métier ainsi que sa propre base de données.

Le projet est organisé en trois parties principales :

### Frontend
Interface utilisateur permettant aux touristes et aux professionnels d'accéder aux différentes fonctionnalités de la plateforme.

### Backend
Ensemble de sept microservices indépendants assurant la gestion des fonctionnalités métier et la communication entre les différents composants.

### Documentation
Contient les diagrammes UML, les diagrammes de classes, les diagrammes de séquence et les documents de présentation du projet.

## 📂 4. Structure du projet

```text
GuideMe/
│
├── Documentation/
│   ├── Diagrams/
│   └── Presentation/
│
├── Frontend/
│
├── backend/
│   ├── ms1-users/
│   ├── ms2-activities-offers/
│   ├── ms3-reservations-payments/
│   ├── ms4/
│   ├── ms5/
│   ├── ms6-posts/
│   └── ms7-notifications/
│
├── .gitignore
└── README.md
```

## ⚙️ 5. Microservices et répartition des responsabilités

Le backend est composé de sept microservices, répartis entre les membres de l'équipe selon leurs domaines de responsabilité.

| Membre | Microservice | Base de données | Responsabilités |
|---|---|---|---|
| **Islem Somai** | MS1 – Utilisateurs et profils professionnels | MySQL | Inscription, gestion des profils, documents légaux et validation des comptes. |
| **Maryem Sghiri** | MS2 – Produits et offres touristiques | H2 | Gestion des activités, des circuits et des offres touristiques. |
| **Emna Ben Hassine** | MS3 – Réservations et paiements | MySQL | Gestion des réservations, des paiements et des remboursements. |
| **Zeineb Soltani** | MS4 – Missions et offres | H2 | Gestion des missions, des offres de prestation et des invitations. |
| **Rami Snoussi** | MS5 – Recommandation intelligente IA | H2 | Génération de recommandations d'activités et gestion de leur historique. |
| **Aziz Zarrouk** | MS6 – Posts et commentaires | MySQL | Gestion du forum, des publications et des commentaires. |
| **Toute l'équipe** | MS7 – Réclamations, conflits et notifications | PostgreSQL | Gestion des réclamations, traitement des conflits et gestion des notifications. |

### Détail des microservices

**MS1 – Utilisateurs et profils professionnels**
- Inscription et authentification des utilisateurs.
- Gestion des informations personnelles.
- Gestion des profils professionnels.
- Gestion des documents légaux.
- Validation des comptes professionnels.

**MS2 – Produits et offres touristiques**
- Gestion des activités touristiques.
- Gestion des circuits touristiques.
- Création et gestion des offres.
- Consultation des produits touristiques.

**MS3 – Réservations et paiements**
- Création et gestion des réservations.
- Suivi du statut des réservations.
- Gestion des paiements.
- Gestion des remboursements.

**MS4 – Missions et offres**
- Gestion des missions.
- Publication des offres de prestation.
- Gestion des invitations.
- Suivi des propositions et des missions.

**MS5 – Recommandation intelligente IA**
- Recommandation intelligente d'activités touristiques.
- Personnalisation des suggestions.
- Gestion de l'historique des recommandations.

**MS6 – Posts et commentaires**
- Création et gestion des publications.
- Consultation du forum.
- Ajout et gestion des commentaires.
- Interaction entre les utilisateurs.

**MS7 – Réclamations, conflits et notifications**
- Dépôt et gestion des réclamations.
- Suivi et traitement des conflits.
- Gestion des notifications.
- Information des utilisateurs concernant les événements et les changements de statut.

## 🛠️ 6. Technologies et outils utilisés

Les technologies sont réparties selon les besoins des différents composants du projet.

| Composant | Technologies / Outils |
|---|---|
| Architecture backend | Microservices |
| Communication interservices | API REST, OpenFeign |
| Bases de données | MySQL, H2, PostgreSQL |
| Gestion de versions | Git, GitHub |
| Modélisation | UML, PlantUML |
| Frontend | Selon l'implémentation du projet |
| Backend | Selon les technologies utilisées dans chaque microservice |

### Bases de données

Chaque microservice dispose d'une base de données adaptée à ses besoins :

- **MySQL** : MS1, MS3 et MS6.
- **H2** : MS2, MS4 et MS5.
- **PostgreSQL** : MS7.

Cette séparation permet de limiter le couplage entre les services et de favoriser leur indépendance.

## 🔄 7. Communication entre les microservices

Les microservices communiquent à travers des interfaces API REST.

**OpenFeign** est utilisé pour faciliter les appels entre les services backend concernés.

Cette organisation permet :
- La communication entre les différents domaines fonctionnels.
- La séparation des responsabilités.
- L'indépendance des microservices.
- Une meilleure évolutivité et une maintenance facilitée.

## 🚀 8. Installation et lancement

### Prérequis

Avant de lancer le projet, il est nécessaire de disposer des outils et environnements requis par les différents composants :

- Git.
- Environnement de développement adapté au frontend.
- Environnement Java et Maven si utilisés par les microservices.
- MySQL.
- PostgreSQL.

H2 est utilisé pour les microservices concernés.

### Étape 1 : Cloner le dépôt

```bash
git clone <URL_DU_DEPOT>
```

### Étape 2 : Accéder au projet

```bash
cd GuideMe
```

### Étape 3 : Lancer le backend

Accéder au dossier `backend`, puis ouvrir le microservice souhaité.

Chaque microservice doit être configuré avec sa base de données et ses paramètres de connexion avant son lancement.

Consulter le README ou les fichiers de configuration de chaque microservice pour connaître les instructions spécifiques.

### Étape 4 : Lancer le frontend

Accéder au dossier `Frontend` et suivre les instructions d'installation et de lancement correspondant à son environnement technique.

## 👥 9. Équipe projet

GuideMe est un projet académique réalisé dans le cadre du cycle ingénieur à l'**École Supérieure Privée d'Ingénierie et de Technologies (ESPRIT)**.

| Membre | Responsabilité |
|---|---|
| Islem Somai | MS1 – Utilisateurs et profils professionnels |
| Maryem Sghiri | MS2 – Produits et offres touristiques |
| Emna Ben Hassine | MS3 – Réservations et paiements |
| Zeineb Soltani | MS4 – Missions et offres |
| Rami Snoussi | MS5 – Recommandation intelligente IA |
| Aziz Zarrouk | MS6 – Posts et commentaires |
| Toute l'équipe | MS7 – Réclamations, conflits et notifications |

---

## 🌍 10. Notre vision

GuideMe ambitionne de rendre les expériences touristiques tunisiennes plus accessibles, plus visibles et plus faciles à réserver.

En combinant réservation en ligne, intelligence artificielle et interaction communautaire, la plateforme propose une nouvelle manière de découvrir et de vivre le tourisme en Tunisie.

<p align="center">
  <strong>🌍 GuideMe – Découvrez la Tunisie autrement !</strong>
</p>
