# ms6-posts (frontend)

Interface du microservice **Publications** (espace communautaire), branchée sur l'API ms6 (`http://localhost:8086/api`).

- `communaute/` : page « Communauté » (`/communaute`) : fil de publications filtrable par statut, création (publiée ou brouillon), modification, publication / archivage, suppression, commentaires et réponses imbriquées
- `post.service.ts` : appels HTTP vers ms6 (posts et commentaires) + `messageErreur()` pour afficher les erreurs du backend
- `post.model.ts` : types `Post`, `Commentaire` et requêtes

En attendant l'authentification (ms1), l'auteur est lu depuis `localStorage.user.id`, sinon choisi dans le champ « Utilisateur n° » de la page.

Pour tester : lancer ms6 (`backend/ms6-posts` → `.\mvnw spring-boot:run`) puis `ng serve` et ouvrir http://localhost:4200/communaute.

À venir : réactions (« j'aime »), qui nécessitent un endpoint côté ms6.
