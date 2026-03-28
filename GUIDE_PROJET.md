# Guide Complet du Projet — Gestion Scolaire API REST

> Mini Projet WS REST — MBDS 2025-2026
> Spring Boot 3.4.3 · Java 17 · H2 · JWT · HATEOAS · Swagger

---

## Table des matières

1. [Présentation du projet](#1-présentation-du-projet)
2. [Architecture et structure des fichiers](#2-architecture-et-structure-des-fichiers)
3. [Installation et lancement](#3-installation-et-lancement)
4. [Ce qui a été fait (Binôme 1)](#4-ce-qui-a-été-fait--binôme-1)
5. [Ce qui a été fait (Binôme 2)](#5-ce-qui-a-été-fait--binôme-2)
6. [Ce qui a été fait (Tâches communes)](#6-ce-qui-a-été-fait--tâches-communes)
7. [Ce qui reste à faire](#7-ce-qui-reste-à-faire)
8. [Base de données et données initiales](#8-base-de-données-et-données-initiales)
9. [Endpoints complets de l'API](#9-endpoints-complets-de-lapi)
10. [Sécurité et rôles JWT](#10-sécurité-et-rôles-jwt)
11. [Comment tester avec Postman](#11-comment-tester-avec-postman)
12. [Comment tester avec Swagger](#12-comment-tester-avec-swagger)
13. [Flux de travail typique](#13-flux-de-travail-typique)

---

## 1. Présentation du projet

C'est une **API REST** pour un système de gestion scolaire. Elle permet de :

- **S'authentifier** (connexion/inscription avec token JWT)
- **Gérer les étudiants** (CRUD complet, résumé avec agrégation)
- **Gérer les professeurs** (CRUD complet)
- **Gérer les cours** (CRUD + statistiques avec agrégation)
- **Gérer les inscriptions** (inscrire/désinscrire un étudiant à un cours)
- **Gérer les notes** (ajouter/modifier/consulter avec contrôle d'accès par rôle)

Chaque réponse REST inclut des **liens HATEOAS** pour la navigation.

---

## 2. Architecture et structure des fichiers

```
src/main/java/com/example/miniprojetgestionscolaire/
│
├── config/                              ← Configuration
│   ├── SecurityConfig.java              ← Règles d'accès (qui peut accéder à quoi)
│   ├── JwtAuthenticationFilter.java     ← Filtre JWT (vérifie le token à chaque requête)
│   ├── SwaggerConfig.java               ← Configuration Swagger/OpenAPI
│   └── ChargeurDonnees.java             ← Données initiales au démarrage
│
├── controller/                          ← Endpoints REST (reçoit les requêtes HTTP)
│   ├── AuthentificationController.java  ← POST /api/auth/connexion, /inscription
│   ├── EtudiantController.java          ← CRUD /api/etudiants
│   ├── ProfesseurController.java        ← CRUD /api/professeurs
│   ├── CoursController.java             ← CRUD /api/cours + statistiques
│   ├── InscriptionController.java       ← /api/inscriptions
│   └── NoteController.java              ← /api/notes
│
├── service/                             ← Logique métier (les "cerveaux" de l'appli)
│   ├── AuthentificationService.java     ← Connexion + inscription
│   ├── JwtService.java                  ← Génération/validation tokens JWT
│   ├── EtudiantService.java             ← Logique étudiant
│   ├── ProfesseurService.java           ← Logique professeur
│   ├── CoursService.java                ← Logique cours + calcul statistiques
│   ├── InscriptionService.java          ← Logique inscription
│   └── NoteService.java                 ← Logique notes + contrôle d'accès
│
├── repository/                          ← Accès base de données (requêtes SQL auto)
│   ├── UtilisateurRepository.java
│   ├── EtudiantRepository.java
│   ├── ProfesseurRepository.java
│   ├── CoursRepository.java
│   ├── InscriptionRepository.java
│   └── NoteRepository.java
│
├── model/                               ← Entités JPA (tables en base de données)
│   ├── Utilisateur.java                 ← Table utilisateurs
│   ├── Etudiant.java                    ← Table etudiants
│   ├── Professeur.java                  ← Table professeurs
│   ├── Cours.java                       ← Table cours
│   ├── Inscription.java                 ← Table inscriptions
│   └── Note.java                        ← Table notes
│
├── dto/                                 ← Objets de transfert (ce qu'on envoie/reçoit en JSON)
│   ├── ConnexionDTO.java
│   ├── InscriptionUtilisateurDTO.java
│   ├── EtudiantDTO.java
│   ├── ProfesseurDTO.java
│   ├── CoursDTO.java
│   ├── InscriptionDTO.java
│   ├── NoteDTO.java
│   ├── ResumeEtudiantDTO.java           ← Agrégation résumé étudiant
│   └── StatistiquesCoursDTO.java        ← Agrégation statistiques cours
│
├── exception/
│   └── GestionnaireErreurs.java         ← Gestion globale des erreurs
│
└── MiniProjetGestionScolaireApplication.java  ← Point d'entrée
```

**Le flux d'une requête** :
```
Client HTTP → Controller → Service → Repository → Base de données H2
                                ↓
                          Logique métier
                     (validation, calculs,
                      contrôle d'accès)
```

---

## 3. Installation et lancement

### Prérequis
- **JDK 17** (pas JRE, il faut le JDK)
- **Maven** (inclus via `mvnw`)

### Lancer le projet
```bash
# Depuis la racine du projet :
./mvnw spring-boot:run
```

### URLs disponibles après lancement
| URL | Description |
|-----|-------------|
| `http://localhost:8080/swagger-ui.html` | Documentation Swagger interactive |
| `http://localhost:8080/h2-console` | Console base de données (JDBC URL: `jdbc:h2:mem:schooldb`, user: `sa`, pas de mot de passe) |
| `http://localhost:8080/api/...` | Endpoints de l'API |

---

## 4. Ce qui a été fait — Binôme 1

### Authentification
| Élément | Fichier | Description |
|---------|---------|-------------|
| Modèle | `model/Utilisateur.java` | Entité avec nom, email, motDePasse, rôle |
| Repository | `repository/UtilisateurRepository.java` | Recherche par email, vérification doublon |
| Service JWT | `service/JwtService.java` | Génère et valide les tokens JWT (expiration 30min) |
| Service Auth | `service/AuthentificationService.java` | Connexion (retourne token) + inscription |
| Controller | `controller/AuthentificationController.java` | `POST /api/auth/connexion` et `/inscription` |
| DTO | `dto/ConnexionDTO.java` | email + motDePasse |
| DTO | `dto/InscriptionUtilisateurDTO.java` | nom + email + motDePasse |
| Config | `config/SecurityConfig.java` | Règles d'accès par rôle |
| Filtre | `config/JwtAuthenticationFilter.java` | Vérifie le token sur chaque requête |

### Étudiant (CRUD)
| Élément | Fichier | Description |
|---------|---------|-------------|
| Modèle | `model/Etudiant.java` | classe, filière, numéroEtudiant (auto), annéeInscription |
| Repository | `repository/EtudiantRepository.java` | Recherche par classe, filière, utilisateur |
| Service | `service/EtudiantService.java` | CRUD + génération numéro ETU + résumé |
| Controller | `controller/EtudiantController.java` | GET, POST, PUT, DELETE + `/infos` (résumé) |
| DTO | `dto/EtudiantDTO.java` | Données étudiant |
| DTO | `dto/ResumeEtudiantDTO.java` | Résumé avec cours, moyenne, crédits |

### Note
| Élément | Fichier | Description |
|---------|---------|-------------|
| Modèle | `model/Note.java` | note (0-20), commentaire, dateNote (auto) |
| Repository | `repository/NoteRepository.java` | Recherche par étudiant, professeur, calcul moyenne |
| Service | `service/NoteService.java` | CRUD + contrôle d'accès (prof ne voit que sa classe) |
| Controller | `controller/NoteController.java` | POST, PUT, GET par étudiant |
| DTO | `dto/NoteDTO.java` | Données note |

### Autres
| Élément | Fichier |
|---------|---------|
| Gestion erreurs | `exception/GestionnaireErreurs.java` |
| Données initiales | `config/ChargeurDonnees.java` |
| Config Swagger | `config/SwaggerConfig.java` |
| Config BDD | `application.properties` |
| Dépendances | `pom.xml` |

---

## 5. Ce qui a été fait — Binôme 2

### Professeur (CRUD)
| Élément | Fichier | Description |
|---------|---------|-------------|
| Modèle | `model/Professeur.java` | spécialité, classeEnseignée (existait déjà) |
| Repository | `repository/ProfesseurRepository.java` | (existait déjà) |
| Service | `service/ProfesseurService.java` | CRUD complet avec validation rôle PROFESSEUR |
| Controller | `controller/ProfesseurController.java` | GET, POST, PUT, DELETE |
| DTO | `dto/ProfesseurDTO.java` | nom, email, spécialité, classeEnseignée |

### Cours (CRUD + Statistiques)
| Élément | Fichier | Description |
|---------|---------|-------------|
| Modèle | `model/Cours.java` | nom, description, crédits, professeur |
| Repository | `repository/CoursRepository.java` | Recherche par professeur, par nom |
| Service | `service/CoursService.java` | CRUD + calcul statistiques (moyenne, max, min) |
| Controller | `controller/CoursController.java` | GET, POST, PUT, DELETE + `/statistiques` |
| DTO | `dto/CoursDTO.java` | Données cours |
| DTO | `dto/StatistiquesCoursDTO.java` | Agrégation : moyenne, max, min, nb étudiants |

### Inscription
| Élément | Fichier | Description |
|---------|---------|-------------|
| Modèle | `model/Inscription.java` | étudiant + cours + dateInscription (auto) |
| Repository | `repository/InscriptionRepository.java` | Recherche par étudiant, par cours, vérif doublon |
| Service | `service/InscriptionService.java` | Inscrire, désinscrire, lister cours d'un étudiant |
| Controller | `controller/InscriptionController.java` | POST, DELETE, GET par étudiant |
| DTO | `dto/InscriptionDTO.java` | Données inscription |

---

## 6. Ce qui a été fait — Tâches communes

| Tâche | Statut | Détail |
|-------|--------|--------|
| `pom.xml` | ✅ Fait | Web, JPA, Security, JWT, H2, Swagger, HATEOAS, Validation |
| Structure du projet | ✅ Fait | Tous les packages créés (config, controller, service, repository, model, dto, exception) |
| HATEOAS | ✅ Fait | Liens de navigation sur **tous les controllers** (self, collection, ressources liées) |
| Documentation Swagger | ✅ Fait | Annotations @Operation, @ApiResponse, @Tag sur tous les endpoints |
| Collection Postman | ✅ Fait | `Gestion_Scolaire_API.postman_collection.json` — 20 requêtes avec tests auto |
| Données initiales | ✅ Fait | 1 admin, 2 profs, 2 étudiants, 3 cours, 4 inscriptions |

---

## 7. Ce qui reste à faire

### Améliorations à considérer (optionnel)

| Tâche | Priorité | Comment faire |
|-------|----------|---------------|
| **Compléter le résumé étudiant** | Moyenne | Dans `EtudiantService.obtenirResume()`, remplacer les placeholders par de vraies données en utilisant `InscriptionRepository` et `NoteRepository` pour calculer la liste de cours, la moyenne, et les crédits |
| **Hashage des mots de passe** | Basse (hors scope projet) | Ajouter `BCryptPasswordEncoder` dans `SecurityConfig`, encoder les mots de passe dans `AuthentificationService.inscrire()`, et comparer avec `matches()` dans `connecter()` |
| **Déploiement en ligne** | Optionnel | Déployer sur Railway ou Render (voir section livraison du PDF). Il faudra remplacer H2 par PostgreSQL et configurer les variables d'environnement |
| **Tests unitaires** | Optionnel | Ajouter des tests JUnit dans `src/test/` pour les services principaux |

### Comment compléter le résumé étudiant

Dans `EtudiantService.java`, méthode `obtenirResume()`, remplacer les lignes placeholder :

```java
// AVANT (placeholder) :
resume.setListeCours(new ArrayList<>());
resume.setMoyenneGenerale(0.0);
resume.setTotalCredits(0);
resume.setNombreCours(0);

// APRÈS (avec vraies données) :
// 1. Injecter InscriptionRepository et NoteRepository dans le service
// 2. Récupérer les inscriptions de l'étudiant
List<Inscription> inscriptions = inscriptionRepository.findByEtudiantId(id);
List<String> nomsCours = inscriptions.stream()
    .map(i -> i.getCours().getNom())
    .collect(Collectors.toList());
int totalCredits = inscriptions.stream()
    .mapToInt(i -> i.getCours().getCredits())
    .sum();
Double moyenne = noteRepository.calculerMoyenneEtudiant(id);

resume.setListeCours(nomsCours);
resume.setMoyenneGenerale(moyenne != null ? moyenne : 0.0);
resume.setTotalCredits(totalCredits);
resume.setNombreCours(nomsCours.size());
```

---

## 8. Base de données et données initiales

### Schéma relationnel
```
UTILISATEURS (id, nom, email, motDePasse, role)
     │
     ├──── 1:1 ──── ETUDIANTS (id, utilisateur_id, classe, filiere, numeroEtudiant, anneeInscription)
     │                    │
     │                    ├──── 1:N ──── INSCRIPTIONS (id, etudiant_id, cours_id, dateInscription)
     │                    │                                       │
     │                    └──── 1:N ──── NOTES (id, etudiant_id, professeur_id, note, commentaire, dateNote)
     │                                          │
     └──── 1:1 ──── PROFESSEURS (id, utilisateur_id, specialite, classeEnseignee)
                          │
                          ├──── 1:N ──── COURS (id, nom, description, credits, professeur_id)
                          │                  │
                          │                  └──── 1:N ──── INSCRIPTIONS
                          │
                          └──── 1:N ──── NOTES
```

### Données chargées au démarrage

| Utilisateur | Email | Mot de passe | Rôle |
|-------------|-------|-------------|------|
| Jean | admin@school.com | admin123 | ADMIN |
| Mika | prof.mika@school.com | prof123 | PROFESSEUR |
| Michel | prof.michel@school.com | prof456 | PROFESSEUR |
| David | etudiant.david@school.com | etu123 | ETUDIANT |
| Marie | etudiant.marie@school.com | etu456 | ETUDIANT |

| Professeur | Spécialité | Classe |
|------------|-----------|--------|
| Mika | Informatique | L3 |
| Michel | Mathématiques | M1 |

| Étudiant | Classe | Filière | N° |
|----------|--------|---------|-----|
| David | L3 | Informatique | ETU000001 |
| Marie | M1 | Mathématiques | ETU000002 |

| Cours | Crédits | Professeur |
|-------|---------|------------|
| Informatique | 3 | Mika |
| Mathématiques | 3 | Michel |
| Physique | 3 | Michel |

| Inscription | Étudiant → Cours |
|-------------|------------------|
| David → Informatique |
| David → Mathématiques |
| Marie → Mathématiques |
| Marie → Physique |

---

## 9. Endpoints complets de l'API

### Authentification (`/api/auth`) — PUBLIC
| Méthode | URL | Body | Réponse |
|---------|-----|------|---------|
| POST | `/api/auth/connexion` | `{"email":"...", "motDePasse":"..."}` | `{"token": "eyJ..."}` |
| POST | `/api/auth/inscription` | `{"nom":"...", "email":"...", "motDePasse":"..."}` | `{"message":"Compte créé..."}` |

### Étudiants (`/api/etudiants`) — ADMIN requis
| Méthode | URL | Body | Réponse |
|---------|-----|------|---------|
| GET | `/api/etudiants` | — | Liste des étudiants + liens HATEOAS |
| GET | `/api/etudiants/{id}` | — | Détail + liens |
| GET | `/api/etudiants/{id}/infos` | — | Résumé avec cours/moyenne |
| POST | `/api/etudiants` | `{"utilisateurId":4, "classe":"L3", "filiere":"Info", "anneeInscription":2026}` | 201 + étudiant créé |
| PUT | `/api/etudiants/{id}` | `{"classe":"M1", "filiere":"Info", "anneeInscription":2026}` | Étudiant modifié |
| DELETE | `/api/etudiants/{id}` | — | `{"message":"Etudiant supprimé..."}` |

### Professeurs (`/api/professeurs`)
| Méthode | URL | Accès | Body |
|---------|-----|-------|------|
| GET | `/api/professeurs` | Authentifié | — |
| GET | `/api/professeurs/{id}` | Authentifié | — |
| POST | `/api/professeurs` | ADMIN | `{"utilisateurId":2, "specialite":"Info", "classeEnseignee":"L3"}` |
| PUT | `/api/professeurs/{id}` | ADMIN/PROF | `{"specialite":"Maths", "classeEnseignee":"M1"}` |
| DELETE | `/api/professeurs/{id}` | ADMIN | — |

### Cours (`/api/cours`)
| Méthode | URL | Accès | Body |
|---------|-----|-------|------|
| GET | `/api/cours` | PUBLIC | — |
| GET | `/api/cours/{id}` | PUBLIC | — |
| POST | `/api/cours` | PROF/ADMIN | `{"nom":"Algo", "description":"...", "credits":3, "professeurId":1}` |
| PUT | `/api/cours/{id}` | PROF/ADMIN | `{"nom":"...", "description":"...", "credits":4, "professeurId":1}` |
| DELETE | `/api/cours/{id}` | ADMIN | — |
| GET | `/api/cours/{id}/statistiques` | Authentifié | — → `{"moyenneClasse":14.2, "meilleureNote":18.0, ...}` |

### Inscriptions (`/api/inscriptions`) — Authentifié
| Méthode | URL | Body |
|---------|-----|------|
| POST | `/api/inscriptions` | `{"etudiantId":1, "coursId":1}` |
| DELETE | `/api/inscriptions/{id}` | — |
| GET | `/api/inscriptions/etudiant/{etudiantId}` | — |

### Notes (`/api/notes`) — ADMIN ou PROFESSEUR
| Méthode | URL | Body |
|---------|-----|------|
| POST | `/api/notes` | `{"etudiantId":1, "note":15.5, "commentaire":"Bon travail"}` |
| PUT | `/api/notes/{id}` | `{"etudiantId":1, "note":17.0, "commentaire":"Excellent !"}` |
| GET | `/api/notes/etudiant/{etudiantId}` | — |

---

## 10. Sécurité et rôles JWT

### Les 3 rôles
| Rôle | Ce qu'il peut faire |
|------|---------------------|
| **ADMIN** | Tout : CRUD étudiants, professeurs, cours, notes, inscriptions |
| **PROFESSEUR** | Créer/modifier cours, ajouter/modifier notes (uniquement ses étudiants) |
| **ETUDIANT** | Voir les cours (public), s'inscrire, voir ses notes |

### Comment fonctionne l'authentification

```
1. Le client envoie POST /api/auth/connexion avec email + mot de passe
2. Le serveur vérifie les identifiants et retourne un token JWT
3. Le client inclut ce token dans chaque requête : Authorization: Bearer <token>
4. Le filtre JwtAuthenticationFilter intercepte la requête :
   - Extrait le token du header
   - Vérifie sa validité (signature + expiration)
   - Extrait l'email et le rôle
   - Met l'utilisateur authentifié dans le SecurityContext
5. SecurityConfig vérifie que le rôle a accès à l'endpoint demandé
6. Le service peut aussi vérifier des règles métier (ex: prof ne note que sa classe)
```

---

## 11. Comment tester avec Postman

1. **Importer** le fichier `Gestion_Scolaire_API.postman_collection.json` dans Postman
2. **Lancer l'application** (`./mvnw spring-boot:run`)
3. **Exécuter dans l'ordre** :
   - D'abord "Connexion ADMIN" → le token est sauvegardé automatiquement dans `{{adminToken}}`
   - Puis "Connexion PROFESSEUR" → sauvegardé dans `{{profToken}}`
   - Puis "Connexion ETUDIANT" → sauvegardé dans `{{etudiantToken}}`
   - Ensuite vous pouvez tester tous les autres endpoints
4. **Les tests automatiques** vérifient les status codes et la présence des liens HATEOAS

---

## 12. Comment tester avec Swagger

1. Lancer l'application
2. Aller sur `http://localhost:8080/swagger-ui.html`
3. Cliquer sur **Authorize** (cadenas en haut à droite)
4. D'abord se connecter via l'endpoint `/api/auth/connexion`
5. Copier le token retourné
6. Le coller dans le champ "Bearer Authentication" de Swagger
7. Tester les endpoints directement depuis l'interface

---

## 13. Flux de travail typique

```
1. ADMIN se connecte → obtient un token
2. ADMIN crée un utilisateur PROFESSEUR via /api/auth/inscription (puis modifier le rôle en BDD)
   OU directement en base de données via H2 console
3. ADMIN crée le profil professeur → POST /api/professeurs
4. Un utilisateur ETUDIANT s'inscrit → POST /api/auth/inscription
5. ADMIN crée le profil étudiant → POST /api/etudiants
6. PROFESSEUR crée un cours → POST /api/cours
7. Un utilisateur inscrit l'étudiant au cours → POST /api/inscriptions
8. PROFESSEUR ajoute une note → POST /api/notes
9. On consulte les statistiques du cours → GET /api/cours/{id}/statistiques
10. On consulte le résumé de l'étudiant → GET /api/etudiants/{id}/infos
```
