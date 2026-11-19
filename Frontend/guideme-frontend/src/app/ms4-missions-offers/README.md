# ms4-missions-offers (frontend)

Interface du microservice **Missions & Offres de mission** (guides, chauffeurs…).

Le tableau de bord des guides et chauffeurs est disponible sur `/professionnel`. Le lien **Missions** ouvre les missions disponibles sur `/missions`.

L’accueil, les missions, les offres et les invitations ont chacun leur composant de page dans `espace-professionnel/`. Ils partagent la navigation et l’état des missions pendant la navigation.

La page **Mes offres** est disponible sur `/professionnel/offres`. On peut filtrer, créer, consulter, modifier et supprimer des offres, ainsi que sélectionner ses jours de disponibilité. Les offres et les jours sélectionnés sont conservés dans le stockage local du navigateur pour la démonstration.

Le dossier `espace-professionnel/` contient les pages destinées aux guides et chauffeurs. Le dossier `espace-prestataire/` contient le tableau de bord de gestion des missions pour les agences, accessible à `/prestataire/missions` depuis la navigation prestataire. Les missions et invitations de cette page utilisent les données de démonstration.

La page présente les missions disponibles, les invitations et les offres de prestation avec les données de la maquette. Les filtres, favoris, candidatures et réponses aux invitations fonctionnent localement le temps de la session sur la page.
