# Guide d'Installation et de Test — Gestion Scolaire API

> Ce document est destiné a une personne qui recupere le projet pour la premiere fois et veut le lancer et le tester.

---

## Table des matieres

1. [Prerequis a installer](#1-prerequis-a-installer)
2. [Recuperer le projet](#2-recuperer-le-projet)
3. [Lancer l'application](#3-lancer-lapplication)
4. [Verifier que tout fonctionne](#4-verifier-que-tout-fonctionne)
5. [Tester avec Swagger (navigateur)](#5-tester-avec-swagger-navigateur)
6. [Tester avec Postman](#6-tester-avec-postman)
7. [Acceder a la base de donnees H2](#7-acceder-a-la-base-de-donnees-h2)
8. [Comptes disponibles au demarrage](#8-comptes-disponibles-au-demarrage)
9. [Scenario de test complet pas a pas](#9-scenario-de-test-complet-pas-a-pas)
10. [Problemes courants et solutions](#10-problemes-courants-et-solutions)

---

## 1. Prerequis a installer

### a) JDK 17 (obligatoire)

**Windows :**
1. Telecharger JDK 17 depuis https://adoptium.net/ (Temurin 17)
2. Lancer l'installeur, cocher "Set JAVA_HOME variable"
3. Verifier l'installation :
```bash
java -version
# Doit afficher : openjdk version "17.x.x"
```

**Mac :**
```bash
brew install openjdk@17
```

**Linux (Ubuntu/Debian) :**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

### b) Maven (normalement inclus)

Le projet inclut un wrapper Maven (`mvnw` / `mvnw.cmd`), donc **pas besoin d'installer Maven separement**.

Si vous voulez utiliser Maven global quand meme :
```bash
# Verifier si Maven est installe
mvn -version
# Doit afficher : Apache Maven 3.x.x
```

### c) Postman (recommande pour les tests)

1. Telecharger Postman depuis https://www.postman.com/downloads/
2. Installer et creer un compte (gratuit)

### d) Un IDE (optionnel mais recommande)

- **IntelliJ IDEA** (Community ou Ultimate) — Recommande pour Java
- **VS Code** avec l'extension "Extension Pack for Java"
- **Eclipse** ou **Spring Tool Suite**

### e) Git (pour cloner le projet)

```bash
git --version
# Si pas installe : https://git-scm.com/downloads
```

---

## 2. Recuperer le projet

### Option A : Cloner depuis GitHub
```bash
git clone <URL_DU_REPO>
cd ws
```

### Option B : Decompresser une archive
Si vous avez recu un fichier .zip :
1. Decompresser le fichier
2. Ouvrir un terminal dans le dossier `ws/`

### Verifier la structure
```bash
ls src/main/java/com/example/miniprojetgestionscolaire/
# Doit afficher : config  controller  dto  exception  model  repository  service  MiniProjetGestionScolaireApplication.java
```

---

## 3. Lancer l'application

### Depuis le terminal

**Windows :**
```bash
cd ws
mvnw.cmd spring-boot:run
```

**Mac / Linux :**
```bash
cd ws
chmod +x mvnw
./mvnw spring-boot:run
```

**Avec Maven global :**
```bash
mvn spring-boot:run
```

### Depuis IntelliJ IDEA
1. **File** > **Open** > selectionner le dossier `ws/`
2. Attendre que Maven telecharge les dependances (barre de progression en bas)
3. Ouvrir `src/main/java/.../MiniProjetGestionScolaireApplication.java`
4. Cliquer sur le bouton vert **Run** (triangle vert) a cote du `main()`

### Depuis VS Code
1. Ouvrir le dossier `ws/` dans VS Code
2. Installer l'extension "Extension Pack for Java" si propose
3. Ouvrir `MiniProjetGestionScolaireApplication.java`
4. Cliquer sur **Run** au-dessus du `main()`

### Ce que vous devez voir dans la console
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

...
Hibernate: create table utilisateurs ...
Hibernate: create table etudiants ...
Hibernate: create table professeurs ...
Hibernate: create table cours ...
Hibernate: create table inscriptions ...
Hibernate: create table notes ...
...
========================================
   Données initiales chargées avec succès !
========================================
...
Started MiniProjetGestionScolaireApplication in X.XXX seconds
```

L'application tourne sur **http://localhost:8080**.

---

## 4. Verifier que tout fonctionne

Ouvrir un navigateur et acceder a ces URLs :

| URL | Ce que vous devez voir |
|-----|----------------------|
| http://localhost:8080/swagger-ui.html | Interface Swagger avec tous les endpoints |
| http://localhost:8080/h2-console | Page de connexion a la base H2 |
| http://localhost:8080/api/cours | Liste des cours en JSON (endpoint public) |

Si http://localhost:8080/api/cours retourne du JSON avec 3 cours, **l'application fonctionne correctement**.

---

## 5. Tester avec Swagger (navigateur)

Swagger est une interface web integree qui permet de tester tous les endpoints directement depuis le navigateur.

### Etape 1 : Ouvrir Swagger
Aller sur : **http://localhost:8080/swagger-ui.html**

Vous verrez les categories :
- Authentification
- Etudiants
- Professeurs
- Cours
- Inscriptions
- Notes

### Etape 2 : Se connecter (obtenir un token)

1. Derouler la section **Authentification**
2. Cliquer sur **POST /api/auth/connexion**
3. Cliquer sur **Try it out**
4. Dans le body, mettre :
```json
{
    "email": "admin@school.com",
    "motDePasse": "admin123"
}
```
5. Cliquer sur **Execute**
6. Dans la reponse, **copier la valeur du token** (la longue chaine de caracteres)

### Etape 3 : Autoriser les requetes

1. Cliquer sur le bouton **Authorize** (cadenas en haut a droite de la page)
2. Dans le champ "Value", taper : `Bearer ` suivi du token copie
   ```
   Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1...
   ```
   (Attention a l'espace apres "Bearer")
3. Cliquer sur **Authorize** puis **Close**

### Etape 4 : Tester les endpoints

Maintenant vous pouvez tester tous les endpoints :
- Cliquer sur un endpoint (ex: GET /api/etudiants)
- Cliquer sur **Try it out**
- Cliquer sur **Execute**
- Voir la reponse en JSON avec les liens HATEOAS

---

## 6. Tester avec Postman

### Etape 1 : Importer la collection

1. Ouvrir Postman
2. Cliquer sur **Import** (en haut a gauche)
3. Glisser-deposer le fichier `Gestion_Scolaire_API.postman_collection.json`
4. La collection "Gestion Scolaire API" apparait dans le panneau gauche

### Etape 2 : Comprendre la collection

La collection contient **20 requetes** organisees en 5 dossiers :

```
Gestion Scolaire API/
├── Authentification/
│   ├── Connexion ADMIN                    ← A executer EN PREMIER
│   ├── Connexion PROFESSEUR (Mika)        ← A executer EN DEUXIEME
│   ├── Connexion ETUDIANT (David)         ← A executer EN TROISIEME
│   ├── Inscription nouveau compte
│   └── Connexion - identifiants invalides
├── Etudiants/
│   ├── Liste tous les etudiants (ADMIN)
│   ├── Detail etudiant par ID
│   ├── Resume etudiant (agregation)
│   └── Modifier etudiant
├── Professeurs/
│   ├── Liste tous les professeurs
│   ├── Detail professeur par ID
│   └── Modifier professeur
├── Cours/
│   ├── Liste tous les cours (PUBLIC)
│   ├── Detail cours par ID
│   ├── Creer un cours (PROFESSEUR)
│   ├── Modifier un cours
│   ├── Supprimer un cours (ADMIN)
│   └── Statistiques cours (agregation)
├── Inscriptions/
│   ├── Inscrire un etudiant a un cours
│   ├── Cours d'un etudiant
│   └── Desinscrire un etudiant
└── Notes/
    ├── Ajouter une note (PROFESSEUR)
    ├── Modifier une note (PROFESSEUR)
    └── Notes d'un etudiant
```

### Etape 3 : Executer les connexions

**IMPORTANT : les 3 premieres requetes doivent etre executees en premier.**

Les tokens sont sauvegardes automatiquement grace aux scripts de test integres :

1. **Cliquer sur "Connexion ADMIN"** → Send
   - Reponse : `{"token": "eyJ..."}`
   - Le token est automatiquement stocke dans la variable `{{adminToken}}`

2. **Cliquer sur "Connexion PROFESSEUR (Mika)"** → Send
   - Le token est stocke dans `{{profToken}}`

3. **Cliquer sur "Connexion ETUDIANT (David)"** → Send
   - Le token est stocke dans `{{etudiantToken}}`

### Etape 4 : Tester les endpoints

Apres les connexions, vous pouvez executer n'importe quelle requete.
Les tokens sont automatiquement utilises dans les headers `Authorization`.

### Etape 5 : Verifier les tests automatiques

Chaque requete a des **tests integres**. Apres l'execution :
1. Cliquer sur l'onglet **Tests** (a cote de Body dans la reponse)
2. Vous verrez des checks verts : "Status 200", "Contient des liens HATEOAS", etc.

### Etape 6 : Lancer tous les tests d'un coup

1. Clic droit sur la collection "Gestion Scolaire API"
2. Cliquer sur **Run collection**
3. Cliquer sur **Run Gestion Scolaire API**
4. Postman execute toutes les requetes dans l'ordre et affiche les resultats

---

## 7. Acceder a la base de donnees H2

### Ouvrir la console
1. Aller sur : **http://localhost:8080/h2-console**
2. Remplir les champs :
   | Champ | Valeur |
   |-------|--------|
   | JDBC URL | `jdbc:h2:mem:schooldb` |
   | User Name | `sa` |
   | Password | *(laisser vide)* |
3. Cliquer sur **Connect**

### Explorer les donnees
Dans la console SQL, vous pouvez executer des requetes :

```sql
-- Voir tous les utilisateurs
SELECT * FROM UTILISATEURS;

-- Voir tous les etudiants avec leur nom
SELECT e.*, u.nom, u.email FROM ETUDIANTS e JOIN UTILISATEURS u ON e.utilisateur_id = u.id;

-- Voir tous les cours avec le nom du professeur
SELECT c.*, u.nom as professeur FROM COURS c
JOIN PROFESSEURS p ON c.professeur_id = p.id
JOIN UTILISATEURS u ON p.utilisateur_id = u.id;

-- Voir les inscriptions
SELECT u.nom as etudiant, c.nom as cours, i.date_inscription
FROM INSCRIPTIONS i
JOIN ETUDIANTS e ON i.etudiant_id = e.id
JOIN UTILISATEURS u ON e.utilisateur_id = u.id
JOIN COURS c ON i.cours_id = c.id;

-- Voir les notes
SELECT u.nom as etudiant, n.note, n.commentaire, up.nom as professeur
FROM NOTES n
JOIN ETUDIANTS e ON n.etudiant_id = e.id
JOIN UTILISATEURS u ON e.utilisateur_id = u.id
JOIN PROFESSEURS p ON n.professeur_id = p.id
JOIN UTILISATEURS up ON p.utilisateur_id = up.id;
```

> **Note** : La base est en memoire. Les donnees sont perdues a chaque redemarrage de l'application. Les donnees initiales sont rechargees automatiquement par `ChargeurDonnees.java`.

---

## 8. Comptes disponibles au demarrage

L'application charge automatiquement des donnees de test au demarrage :

### Utilisateurs
| Nom | Email | Mot de passe | Role |
|-----|-------|-------------|------|
| Jean | `admin@school.com` | `admin123` | ADMIN |
| Mika | `prof.mika@school.com` | `prof123` | PROFESSEUR |
| Michel | `prof.michel@school.com` | `prof456` | PROFESSEUR |
| David | `etudiant.david@school.com` | `etu123` | ETUDIANT |
| Marie | `etudiant.marie@school.com` | `etu456` | ETUDIANT |

### Professeurs
| Nom | Specialite | Classe enseignee |
|-----|-----------|-----------------|
| Mika | Informatique | L3 |
| Michel | Mathematiques | M1 |

### Etudiants
| Nom | Classe | Filiere | Numero |
|-----|--------|---------|--------|
| David | L3 | Informatique | ETU000001 |
| Marie | M1 | Mathematiques | ETU000002 |

### Cours
| Nom | Credits | Professeur |
|-----|---------|------------|
| Informatique | 3 | Mika |
| Mathematiques | 3 | Michel |
| Physique | 3 | Michel |

### Inscriptions
| Etudiant | Cours |
|----------|-------|
| David | Informatique |
| David | Mathematiques |
| Marie | Mathematiques |
| Marie | Physique |

---

## 9. Scenario de test complet pas a pas

Voici un scenario complet pour tester toutes les fonctionnalites. Utilisez Postman ou Swagger.

### Phase 1 : Authentification

```
1. POST /api/auth/connexion
   Body: {"email": "admin@school.com", "motDePasse": "admin123"}
   → Reponse : {"token": "eyJ..."} → Garder ce token (ADMIN)

2. POST /api/auth/connexion
   Body: {"email": "prof.mika@school.com", "motDePasse": "prof123"}
   → Garder ce token (PROFESSEUR)

3. POST /api/auth/inscription
   Body: {"nom": "Test User", "email": "test@school.com", "motDePasse": "test123"}
   → Reponse 201 : compte cree avec role ETUDIANT

4. POST /api/auth/connexion
   Body: {"email": "admin@school.com", "motDePasse": "mauvais"}
   → Reponse 401 : identifiants invalides
```

### Phase 2 : CRUD Etudiants (token ADMIN)

```
5. GET /api/etudiants
   Header: Authorization: Bearer <adminToken>
   → Liste des 2 etudiants avec liens HATEOAS

6. GET /api/etudiants/1
   → Detail de David avec liens self, notes, inscriptions

7. PUT /api/etudiants/1
   Body: {"classe": "M1", "filiere": "Informatique", "anneeInscription": 2026}
   → Etudiant modifie

8. GET /api/etudiants/1/infos
   → Resume de David (agregation)
```

### Phase 3 : CRUD Professeurs (token ADMIN)

```
9. GET /api/professeurs
   → Liste des 2 professeurs

10. GET /api/professeurs/1
    → Detail de Mika

11. PUT /api/professeurs/1
    Body: {"specialite": "IA", "classeEnseignee": "L3"}
    → Professeur modifie
```

### Phase 4 : CRUD Cours

```
12. GET /api/cours (SANS token — endpoint public)
    → Liste des 3 cours avec liens HATEOAS

13. GET /api/cours/1
    → Detail du cours Informatique avec lien vers statistiques

14. POST /api/cours (token PROFESSEUR)
    Body: {"nom": "Algorithmes", "description": "Structures de donnees", "credits": 4, "professeurId": 1}
    → Cours cree (201)

15. PUT /api/cours/1 (token ADMIN)
    Body: {"nom": "Info Avancee", "description": "...", "credits": 4, "professeurId": 1}
    → Cours modifie

16. DELETE /api/cours/3 (token ADMIN)
    → Cours Physique supprime
```

### Phase 5 : Inscriptions (token ADMIN ou authentifie)

```
17. POST /api/inscriptions
    Body: {"etudiantId": 1, "coursId": 3}
    → Inscription creee (201) avec liens HATEOAS vers etudiant et cours

18. GET /api/inscriptions/etudiant/1
    → Liste des cours de David

19. DELETE /api/inscriptions/5
    → Desinscription reussie
```

### Phase 6 : Notes (token PROFESSEUR)

```
20. POST /api/notes (token prof.mika)
    Body: {"etudiantId": 1, "note": 15.5, "commentaire": "Bon travail"}
    → Note creee (201) — Mika note David car David est en L3 (classe de Mika)

21. PUT /api/notes/1 (token prof.mika)
    Body: {"etudiantId": 1, "note": 17.0, "commentaire": "Excellent !"}
    → Note modifiee

22. GET /api/notes/etudiant/1 (token prof.mika)
    → Notes de David (filtrees par le prof connecte)

23. GET /api/notes/etudiant/1 (token ADMIN)
    → TOUTES les notes de David (pas de filtre pour l'admin)
```

### Phase 7 : Agregations

```
24. GET /api/cours/1/statistiques (token ADMIN)
    → Reponse :
    {
        "id": 1,
        "nomCours": "Informatique",
        "professeur": "Mika",
        "nombreEtudiants": 2,
        "moyenneClasse": 15.5,
        "meilleureNote": 17.0,
        "noteMinimale": 14.0
    }

25. GET /api/etudiants/1/infos (token ADMIN)
    → Resume etudiant avec liste de cours, moyenne, credits
```

---

## 10. Problemes courants et solutions

### "Port 8080 already in use"
Un autre programme utilise le port 8080.

**Solution :**
```bash
# Windows : trouver et tuer le processus
netstat -ano | findstr :8080
taskkill /PID <numero_PID> /F

# Mac/Linux :
lsof -i :8080
kill -9 <PID>
```

Ou changer le port dans `application.properties` :
```properties
server.port=8081
```

### "JAVA_HOME is not set"

**Windows :**
1. Panneau de configuration > Systeme > Variables d'environnement
2. Ajouter une variable systeme :
   - Nom : `JAVA_HOME`
   - Valeur : `C:\Program Files\Eclipse Adoptium\jdk-17.x.x` (adapter le chemin)
3. Ajouter `%JAVA_HOME%\bin` au `PATH`
4. Redemarrer le terminal

**Mac/Linux :**
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
# Ajouter cette ligne dans ~/.bashrc ou ~/.zshrc
```

### "mvnw: Permission denied" (Mac/Linux)
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

### Le token JWT a expire (erreur 401 apres 30 min)
Le token expire apres 30 minutes. Il suffit de se reconnecter :
```
POST /api/auth/connexion avec les identifiants
```
Dans Postman, re-executer les 3 requetes de connexion.

### "Access Denied" / Erreur 403
Vous utilisez un token qui n'a pas le bon role pour cet endpoint.
Verifier quel role est necessaire (voir la documentation Swagger ou le GUIDE_PROJET.md).

Exemples :
- `DELETE /api/etudiants/1` → Necessite le token **ADMIN**
- `POST /api/notes` → Necessite le token **PROFESSEUR**
- `GET /api/cours` → Aucun token necessaire (public)

### La base H2 ne contient pas les donnees
La base est en memoire : les donnees sont rechargees a chaque demarrage.
Si vous avez modifie `ChargeurDonnees.java`, verifiez qu'il n'y a pas d'erreur dans la console.

### Swagger n'affiche pas tous les endpoints
1. Verifier que l'application est bien lancee
2. Vider le cache du navigateur (Ctrl+F5)
3. Essayer l'URL directe : http://localhost:8080/swagger-ui/index.html

### H2 Console : "Database not found"
Verifier que le JDBC URL est exactement : `jdbc:h2:mem:schooldb`
(pas `jdbc:h2:~/schooldb` ni autre chose)

### IntelliJ ne reconnait pas le projet
1. **File** > **Invalidate Caches** > **Invalidate and Restart**
2. Clic droit sur `pom.xml` > **Maven** > **Reload Project**
3. Verifier que le JDK 17 est configure : **File** > **Project Structure** > **Project SDK**
