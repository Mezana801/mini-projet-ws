# Répartition des tâches — API REST Spring Boot
## Système de Gestion Scolaire — MBDS 2025-2026
**Spring Boot 3.4.3 | JDK 17**

---

## Binôme 1 — Fanomezantsoa Soloniaina ANDRIAMBOAVONJY

- Mise en place de la sécurité JWT : génération du token, validation, extraction de l'email et du rôle
- Filtre de sécurité JWT appliqué sur toutes les requêtes HTTP
- Configuration Spring Security : gestion des routes publiques et protégées selon les rôles
- Endpoint de connexion : authentification par email et mot de passe avec retour du token JWT
- Endpoint d'inscription : création de compte avec rôle ETUDIANT par défaut
- Validation des données sur les formulaires de connexion et d'inscription
- Contrainte d'unicité sur l'email en base de données
- Gestion globale des erreurs : validation, contrainte unique, erreurs serveur
- CRUD complet sur les étudiants : création, lecture, modification, suppression
- Génération automatique du numéro étudiant en séquence
- Endpoint d'agrégation résumé étudiant : moyenne générale et liste des cours inscrits
- CRUD complet sur les notes : ajout, modification, consultation par étudiant
- Restriction d'accès sur les notes : un professeur ne voit que les notes de sa classe
- Endpoint de consultation de toutes les notes filtré automatiquement selon le rôle connecté
- HATEOAS sur tous les endpoints Authentification
- Chargement automatique des données initiales au démarrage de l'application
- Déploiement de l'API en ligne sur Render

---

## Binôme 2 — ANDRIAMASY Miadantsoa Salema

- CRUD complet sur les professeurs : création, lecture, modification, suppression
- CRUD complet sur les cours : création, lecture, modification, suppression
- Gestion des inscriptions : inscription d'un étudiant à un cours, désinscription, consultation des cours d'un étudiant
- Endpoint d'agrégation statistiques cours : nombre d'étudiants, moyenne de classe, note maximale et note minimale
- HATEOAS sur tous les endpoints Professeur, Cours, Inscription, Etudiant et Note
- Modèle de données Professeur lié à un utilisateur avec classe enseignée et spécialité
- Modèle de données Cours lié à un professeur avec crédits et description
- Modèle de données Inscription représentant la relation n à n entre Etudiant et Cours
- Collection Postman exportée pour la livraison

---

