# Révision des Notions — Projet Gestion Scolaire API REST

> Toutes les notions abordées dans le projet, expliquées simplement pour réviser.

---

## Table des matières

1. [Spring Boot — Les bases](#1-spring-boot--les-bases)
2. [Architecture REST](#2-architecture-rest)
3. [JPA et Hibernate — Base de données](#3-jpa-et-hibernate--base-de-données)
4. [Relations entre entités](#4-relations-entre-entités)
5. [Repository — Accès aux données](#5-repository--accès-aux-données)
6. [DTO — Data Transfer Object](#6-dto--data-transfer-object)
7. [Validation des données](#7-validation-des-données)
8. [Gestion des erreurs](#8-gestion-des-erreurs)
9. [Sécurité — Spring Security](#9-sécurité--spring-security)
10. [JWT — JSON Web Token](#10-jwt--json-web-token)
11. [HATEOAS](#11-hateoas)
12. [Swagger / OpenAPI](#12-swagger--openapi)
13. [Injection de dépendances](#13-injection-de-dépendances)
14. [Annotations clés à connaître](#14-annotations-clés-à-connaître)
15. [Questions d'examen possibles](#15-questions-dexamen-possibles)

---

## 1. Spring Boot — Les bases

### C'est quoi ?
Spring Boot est un framework Java qui simplifie la création d'applications web. Il fournit une configuration automatique : on écrit du code métier, pas de la plomberie.

### Le fichier `pom.xml`
C'est le fichier de configuration Maven. Il déclare les **dépendances** (les bibliothèques) du projet.

```xml
<!-- Exemple : ajouter le support REST -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Les "starters" sont des packs de dépendances pré-configurées :
| Starter | Ce qu'il apporte |
|---------|-----------------|
| `spring-boot-starter-web` | Serveur web embarqué (Tomcat), REST, JSON |
| `spring-boot-starter-data-jpa` | ORM Hibernate, accès base de données |
| `spring-boot-starter-security` | Authentification, autorisation |
| `spring-boot-starter-validation` | Validation des données (@NotBlank, @Min...) |
| `spring-boot-starter-hateoas` | Liens hypermedia dans les réponses REST |

### `application.properties`
Fichier de configuration de l'application. Exemples :
```properties
spring.datasource.url=jdbc:h2:mem:schooldb    # URL de la base de données
spring.jpa.hibernate.ddl-auto=create-drop      # Recrée le schéma à chaque démarrage
spring.jpa.show-sql=true                       # Affiche les requêtes SQL dans la console
```

### Point d'entrée
```java
@SpringBootApplication   // Active la config automatique
public class MiniProjetGestionScolaireApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniProjetGestionScolaireApplication.class, args);
    }
}
```

---

## 2. Architecture REST

### C'est quoi REST ?
**REST** (Representational State Transfer) est un style d'architecture pour les API web. Chaque ressource (étudiant, cours, note) a une URL unique et on utilise les méthodes HTTP pour agir dessus.

### Les méthodes HTTP
| Méthode | Action | Exemple |
|---------|--------|---------|
| **GET** | Lire / Récupérer | `GET /api/etudiants` → liste des étudiants |
| **POST** | Créer | `POST /api/etudiants` → créer un étudiant |
| **PUT** | Modifier | `PUT /api/etudiants/1` → modifier l'étudiant 1 |
| **DELETE** | Supprimer | `DELETE /api/etudiants/1` → supprimer l'étudiant 1 |

### Les codes de réponse HTTP
| Code | Signification | Quand l'utiliser |
|------|--------------|-----------------|
| **200** | OK | Requête réussie (GET, PUT) |
| **201** | Created | Ressource créée (POST) |
| **400** | Bad Request | Données invalides |
| **401** | Unauthorized | Pas de token ou token invalide |
| **403** | Forbidden | Token valide mais pas le bon rôle |
| **404** | Not Found | Ressource inexistante |
| **500** | Server Error | Erreur interne |

### Anatomie d'un Controller
```java
@RestController                          // Dit à Spring que c'est un controller REST
@RequestMapping("/api/etudiants")        // URL de base pour tous les endpoints
@Tag(name = "Étudiants")                 // Catégorie dans Swagger
public class EtudiantController {

    private final EtudiantService service;  // Injection du service

    @GetMapping                           // Répond aux GET /api/etudiants
    public ResponseEntity<...> lister() {
        return ResponseEntity.ok(service.findAll());  // Retourne 200 + données
    }

    @PostMapping                          // Répond aux POST /api/etudiants
    public ResponseEntity<...> creer(@Valid @RequestBody EtudiantDTO dto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)   // Retourne 201
            .body(service.creer(dto));
    }

    @PutMapping("/{id}")                  // Répond aux PUT /api/etudiants/1
    public ResponseEntity<...> modifier(@PathVariable Long id, @Valid @RequestBody EtudiantDTO dto) {
        return ResponseEntity.ok(service.modifier(id, dto));
    }

    @DeleteMapping("/{id}")               // Répond aux DELETE /api/etudiants/1
    public ResponseEntity<...> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.ok(Map.of("message", "Supprimé"));
    }
}
```

### Points clés à retenir
- `@RequestBody` : le JSON envoyé par le client est converti en objet Java
- `@PathVariable` : récupère la valeur dans l'URL (ex: `{id}` → `Long id`)
- `@Valid` : active la validation des annotations du DTO
- `ResponseEntity` : permet de contrôler le code HTTP de la réponse

---

## 3. JPA et Hibernate — Base de données

### C'est quoi JPA ?
**JPA** (Java Persistence API) est une spécification qui permet de mapper des objets Java vers des tables en base de données. **Hibernate** est l'implémentation utilisée par Spring Boot.

### C'est quoi un ORM ?
Un **ORM** (Object-Relational Mapping) traduit automatiquement entre les objets Java et les tables SQL. On n'écrit (presque) pas de SQL.

```
Classe Java  ←→  Table SQL
Attribut     ←→  Colonne
Instance     ←→  Ligne
```

### Anatomie d'une entité
```java
@Entity                              // Dit que cette classe = une table
@Table(name = "etudiants")           // Nom de la table (optionnel)
public class Etudiant {

    @Id                              // Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incrémenté
    private Long id;

    @Column(nullable = false)        // Colonne obligatoire
    private String classe;

    @Column(nullable = false, unique = true)  // Obligatoire + unique
    private String numeroEtudiant;

    // Getters et Setters obligatoires pour JPA
}
```

### H2 — Base de données en mémoire
- Pas besoin d'installer un serveur de BDD
- Les données sont **perdues à chaque redémarrage** (`create-drop`)
- Console accessible à `http://localhost:8080/h2-console`
- Pratique pour le développement et les tests

### `ddl-auto` — Stratégies
| Valeur | Comportement |
|--------|-------------|
| `create-drop` | Crée le schéma au démarrage, le supprime à l'arrêt (notre projet) |
| `create` | Crée le schéma, écrase l'ancien |
| `update` | Met à jour le schéma sans perdre les données |
| `validate` | Vérifie que le schéma correspond aux entités |
| `none` | Ne touche pas au schéma |

---

## 4. Relations entre entités

### Les types de relations JPA

#### OneToOne (1:1) — Un utilisateur a un seul profil étudiant
```java
// Dans Etudiant.java
@OneToOne
@JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
private Utilisateur utilisateur;

// Dans Utilisateur.java (côté inverse, optionnel)
@OneToOne(mappedBy = "utilisateur")
private Etudiant etudiant;
```
> `@JoinColumn` : c'est cette table qui contient la clé étrangère.
> `mappedBy` : l'autre côté de la relation (pas de clé étrangère ici).

#### ManyToOne / OneToMany (N:1) — Plusieurs cours pour un professeur
```java
// Dans Cours.java (le côté "many")
@ManyToOne
@JoinColumn(name = "professeur_id")
private Professeur professeur;

// Dans Professeur.java (le côté "one")
@OneToMany(mappedBy = "professeur")
private List<Cours> cours;
```
> Un cours a **un** professeur. Un professeur a **plusieurs** cours.

#### Contrainte d'unicité composite — Un étudiant ne s'inscrit qu'une fois à un cours
```java
@Entity
@Table(name = "inscriptions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"etudiant_id", "cours_id"})
})
public class Inscription {
    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "cours_id")
    private Cours cours;
}
```

### Schéma des relations du projet
```
Utilisateur ──1:1──→ Etudiant ──1:N──→ Inscription ←──N:1── Cours
                                                              │
Utilisateur ──1:1──→ Professeur ──1:N──→ Cours               │
                         │                                    │
                         └────────1:N──→ Note ←──N:1── Etudiant
```

### @PrePersist — Exécuter du code avant l'insertion
```java
@PrePersist
public void prePersist() {
    this.dateInscription = LocalDateTime.now();  // Date auto avant insertion
}
```

---

## 5. Repository — Accès aux données

### C'est quoi ?
Un Repository est une **interface** qui fournit les opérations CRUD automatiquement. On n'écrit pas d'implémentation : Spring la génère.

```java
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    // JpaRepository fournit automatiquement :
    // findAll(), findById(), save(), deleteById(), count(), existsById()...
}
```

### Méthodes personnalisées par convention de nommage
Spring génère le SQL automatiquement à partir du nom de la méthode :

```java
// SELECT * FROM etudiants WHERE classe = ?
List<Etudiant> findByClasse(String classe);

// SELECT * FROM etudiants WHERE classe = ? AND filiere = ?
List<Etudiant> findByClasseAndFiliere(String classe, String filiere);

// SELECT 1 FROM etudiants WHERE numero_etudiant = ? (retourne true/false)
boolean existsByNumeroEtudiant(String numero);

// SELECT * FROM cours WHERE nom LIKE %?% (insensible à la casse)
List<Cours> findByNomContainingIgnoreCase(String nom);
```

### Requêtes personnalisées avec @Query (JPQL)
Quand le nom de méthode ne suffit pas, on écrit du JPQL (SQL orienté objet) :

```java
// Calcul de la moyenne d'un étudiant
@Query("SELECT AVG(n.note) FROM Note n WHERE n.etudiant.id = :etudiantId")
Double calculerMoyenneEtudiant(@Param("etudiantId") Long etudiantId);

// Notes par classe de l'étudiant
@Query("SELECT n FROM Note n WHERE n.etudiant.classe = :classe")
List<Note> findByClasseEtudiant(@Param("classe") String classe);
```

> **JPQL vs SQL** : en JPQL on utilise les noms des classes Java et leurs attributs, pas les noms des tables/colonnes SQL.

---

## 6. DTO — Data Transfer Object

### Pourquoi utiliser des DTOs ?
Un DTO est un objet qui sert uniquement à **transporter des données** entre le client et le serveur.

**Sans DTO (mauvaise pratique)** :
- On expose l'entité directement → le client voit le mot de passe, les relations internes
- Le client peut modifier des champs qu'il ne devrait pas (ex: id, rôle)

**Avec DTO (bonne pratique)** :
- On choisit exactement quels champs envoyer/recevoir
- On ajoute la validation sur le DTO, pas sur l'entité
- On découple l'API de la base de données

### Exemple concret
```java
// L'entité (en base) contient tout
public class Etudiant {
    private Long id;
    private Utilisateur utilisateur;  // Contient le mot de passe !
    private String classe;
    private String filiere;
    private String numeroEtudiant;
    private int anneeInscription;
}

// Le DTO (envoyé au client) ne contient que le nécessaire
public class EtudiantDTO {
    private Long id;
    private String nom;          // Extrait de utilisateur.nom
    private String email;        // Extrait de utilisateur.email
    private String classe;
    private String filiere;
    private String numeroEtudiant;
    private int anneeInscription;
    private Long utilisateurId;  // Juste l'ID, pas l'objet entier
    // Pas de mot de passe !
}
```

### Conversion Entité ↔ DTO (dans le Service)
```java
private EtudiantDTO etudiantToDto(Etudiant e) {
    EtudiantDTO dto = new EtudiantDTO();
    dto.setId(e.getId());
    dto.setNom(e.getUtilisateur().getNom());
    dto.setEmail(e.getUtilisateur().getEmail());
    dto.setClasse(e.getClasse());
    dto.setFiliere(e.getFiliere());
    dto.setNumeroEtudiant(e.getNumeroEtudiant());
    dto.setAnneeInscription(e.getAnneeInscription());
    return dto;
}
```

---

## 7. Validation des données

### Les annotations de validation (Jakarta Validation)
On les place sur les champs du DTO :

```java
public class EtudiantDTO {
    @NotBlank(message = "La classe est obligatoire")     // Non vide
    private String classe;

    @NotNull(message = "L'année est obligatoire")        // Non null
    @Positive(message = "L'année doit être positive")    // > 0
    private Integer anneeInscription;
}

public class NoteDTO {
    @NotNull
    @Min(value = 0, message = "La note minimum est 0")   // >= 0
    @Max(value = 20, message = "La note maximum est 20") // <= 20
    private Double note;
}

public class ConnexionDTO {
    @NotBlank
    @Email(message = "Email invalide")                    // Format email
    private String email;

    @NotBlank
    @Size(min = 4, message = "Minimum 4 caractères")     // Taille min
    private String motDePasse;
}
```

### Activer la validation dans le Controller
```java
@PostMapping
public ResponseEntity<?> creer(@Valid @RequestBody EtudiantDTO dto) {
    //                          ^^^^^^ Active la validation
    // Si la validation échoue → MethodArgumentNotValidException → 400
}
```

### Gestion centralisée des erreurs de validation
```java
@RestControllerAdvice
public class GestionnaireErreurs {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erreurs = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e ->
            erreurs.put(e.getField(), e.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(erreurs);
        // Retourne : {"classe": "La classe est obligatoire", "anneeInscription": "..."}
    }
}
```

---

## 8. Gestion des erreurs

### `@RestControllerAdvice`
C'est un **gestionnaire global d'exceptions**. Au lieu de gérer les erreurs dans chaque controller, on les centralise :

```java
@RestControllerAdvice
public class GestionnaireErreurs {

    // Erreurs métier (ex: "Étudiant non trouvé")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest()
            .body(Map.of("erreur", ex.getMessage()));
    }

    // Erreurs de validation (ex: champ vide)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(...) { ... }

    // Toutes les autres erreurs
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return ResponseEntity.status(500)
            .body(Map.of("erreur", "Erreur interne du serveur"));
    }
}
```

### Principe : on lance des RuntimeException dans les services
```java
// Dans le service
public EtudiantDTO findById(Long id) {
    Etudiant e = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Étudiant non trouvé avec l'id : " + id));
    return etudiantToDto(e);
}
// → Si l'étudiant n'existe pas, le GestionnaireErreurs attrape l'exception
//   et retourne automatiquement : 400 {"erreur": "Étudiant non trouvé avec l'id : 99"}
```

---

## 9. Sécurité — Spring Security

### C'est quoi ?
Spring Security est un framework qui gère l'**authentification** (qui es-tu ?) et l'**autorisation** (qu'as-tu le droit de faire ?).

### La chaîne de filtres (SecurityFilterChain)
Chaque requête HTTP passe par une série de filtres avant d'atteindre le controller :

```
Requête HTTP → [JwtAuthenticationFilter] → [SecurityConfig rules] → Controller
                     ↑                              ↑
              Vérifie le token              Vérifie le rôle
```

### Configuration dans SecurityConfig
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())                    // Pas de CSRF (API stateless)
        .sessionManagement(s -> s
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Pas de session
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()           // Public
            .requestMatchers(HttpMethod.GET, "/api/cours/**").permitAll()  // GET cours = public
            .requestMatchers("/api/etudiants/**").hasRole("ADMIN")        // Admin only
            .requestMatchers("/api/notes/**").hasAnyRole("ADMIN", "PROFESSEUR")
            .anyRequest().authenticated()                         // Tout le reste = authentifié
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

### Points clés
- **`permitAll()`** : accessible sans authentification
- **`hasRole("ADMIN")`** : seul le rôle ADMIN peut accéder
- **`hasAnyRole("ADMIN", "PROFESSEUR")`** : ADMIN ou PROFESSEUR
- **`authenticated()`** : n'importe quel utilisateur connecté
- **CSRF désactivé** : normal pour une API REST stateless (pas de cookies)
- **Session STATELESS** : pas de session côté serveur, tout passe par le token JWT

---

## 10. JWT — JSON Web Token

### C'est quoi ?
Un JWT est un **jeton** (une chaîne de caractères) qui prouve l'identité de l'utilisateur. Il est envoyé dans chaque requête au lieu d'un cookie de session.

### Structure d'un JWT
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBzY2hvb2wuY29tIiwicm9sZSI6IkFETUlOIn0.abc123
│         HEADER        │                  PAYLOAD                              │  SIGNATURE  │
```

| Partie | Contenu |
|--------|---------|
| **Header** | Algorithme de signature (HS256) |
| **Payload** | Données : email (`sub`), rôle (`role`), expiration (`exp`) |
| **Signature** | Vérification d'intégrité (clé secrète côté serveur) |

### Cycle de vie du JWT dans le projet

```java
// 1. GÉNÉRATION (à la connexion)
public String genererToken(String email, String role) {
    return Jwts.builder()
        .setSubject(email)                                    // Qui
        .claim("role", role)                                  // Quel rôle
        .setIssuedAt(new Date())                              // Quand
        .setExpiration(new Date(System.currentTimeMillis() + 1800000))  // Expire dans 30min
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Signature
        .compact();
}

// 2. VALIDATION (à chaque requête, dans le filtre)
public boolean estValide(String token) {
    try {
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        return true;
    } catch (Exception e) {
        return false;  // Token expiré, modifié, ou invalide
    }
}

// 3. EXTRACTION des infos
public String extraireEmail(String token) { ... }  // Qui est connecté ?
public String extraireRole(String token) { ... }   // Quel est son rôle ?
```

### Le filtre JWT (JwtAuthenticationFilter)
```java
@Override
protected void doFilterInternal(HttpServletRequest request, ...) {
    // 1. Extraire le token du header "Authorization: Bearer eyJ..."
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        // 2. Vérifier la validité
        if (jwtService.estValide(token)) {
            String email = jwtService.extraireEmail(token);
            String role = jwtService.extraireRole(token);

            // 3. Mettre l'utilisateur dans le contexte de sécurité
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(email, null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role)));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }
    filterChain.doFilter(request, response);  // Passer au filtre suivant
}
```

### Pourquoi JWT plutôt que les sessions ?
| Sessions (classique) | JWT (notre projet) |
|---------------------|-------------------|
| État stocké côté serveur | Pas d'état serveur (stateless) |
| Utilise des cookies | Utilise le header Authorization |
| Difficile à scaler | Facile à scaler (pas de partage de session) |
| Fonctionne bien avec les navigateurs | Fonctionne bien avec les API |

---

## 11. HATEOAS

### C'est quoi ?
**HATEOAS** (Hypermedia As The Engine Of Application State) = ajouter des **liens de navigation** dans chaque réponse REST. Le client n'a pas besoin de connaître les URLs à l'avance.

### Sans HATEOAS
```json
{
    "id": 1,
    "nom": "David",
    "classe": "L3"
}
```

### Avec HATEOAS
```json
{
    "id": 1,
    "nom": "David",
    "classe": "L3",
    "_links": {
        "self": { "href": "http://localhost:8080/api/etudiants/1" },
        "etudiants": { "href": "http://localhost:8080/api/etudiants" },
        "notes": { "href": "http://localhost:8080/api/notes/etudiant/1" },
        "inscriptions": { "href": "http://localhost:8080/api/inscriptions/etudiant/1" }
    }
}
```

### Comment implémenter HATEOAS

```java
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

// 1. Wrapper un objet avec EntityModel
EntityModel<EtudiantDTO> model = EntityModel.of(dto);

// 2. Ajouter des liens
model.add(linkTo(methodOn(EtudiantController.class).obtenir(dto.getId())).withSelfRel());
model.add(linkTo(methodOn(EtudiantController.class).lister()).withRel("etudiants"));
model.add(linkTo(methodOn(NoteController.class).obtenirNotes(dto.getId())).withRel("notes"));

// 3. Pour une collection
List<EntityModel<EtudiantDTO>> models = etudiants.stream()
    .map(dto -> {
        EntityModel<EtudiantDTO> m = EntityModel.of(dto);
        m.add(linkTo(methodOn(EtudiantController.class).obtenir(dto.getId())).withSelfRel());
        return m;
    })
    .collect(Collectors.toList());

CollectionModel<EntityModel<EtudiantDTO>> collection = CollectionModel.of(models);
collection.add(linkTo(methodOn(EtudiantController.class).lister()).withSelfRel());
```

### Classes HATEOAS à connaître
| Classe | Usage |
|--------|-------|
| `EntityModel<T>` | Wrapper pour un seul objet + liens |
| `CollectionModel<T>` | Wrapper pour une liste d'objets + liens |
| `WebMvcLinkBuilder.linkTo()` | Génère une URL à partir d'un controller |
| `methodOn()` | Référence une méthode de controller |
| `.withSelfRel()` | Lien "self" (vers soi-même) |
| `.withRel("nom")` | Lien nommé (vers une ressource liée) |

---

## 12. Swagger / OpenAPI

### C'est quoi ?
**Swagger** (OpenAPI) génère automatiquement une **documentation interactive** de l'API. On peut tester les endpoints directement depuis le navigateur.

### Configuration
```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Gestion Scolaire API")
                .version("1.0.0"))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
```

### Annotations Swagger dans les controllers
```java
@Operation(summary = "Liste tous les étudiants",
           description = "Retourne la liste complète")
@ApiResponse(responseCode = "200", description = "Liste récupérée")
@ApiResponse(responseCode = "401", description = "Non authentifié")
@GetMapping
public ResponseEntity<?> lister() { ... }
```

### Accès
- **UI interactive** : `http://localhost:8080/swagger-ui.html`
- **JSON brut** : `http://localhost:8080/v3/api-docs`

---

## 13. Injection de dépendances

### C'est quoi ?
Au lieu de créer les objets nous-mêmes (`new EtudiantService()`), Spring les crée et les **injecte** automatiquement là où on en a besoin.

### Comment ça marche
```java
@Service                                    // Spring crée une instance unique
public class EtudiantService {

    private final EtudiantRepository repository;  // Besoin du repository

    // Spring injecte automatiquement le repository via le constructeur
    public EtudiantService(EtudiantRepository repository) {
        this.repository = repository;
    }
}
```

### Les annotations qui créent des "beans" Spring
| Annotation | Usage |
|------------|-------|
| `@Component` | Bean générique |
| `@Service` | Bean de logique métier |
| `@Repository` | Bean d'accès aux données |
| `@RestController` | Bean de controller REST |
| `@Configuration` | Bean de configuration |
| `@Bean` | Méthode qui retourne un bean |

### Pourquoi c'est important ?
- **Découplage** : le controller ne sait pas comment le service est créé
- **Testabilité** : on peut facilement remplacer une implémentation (mock)
- **Gestion du cycle de vie** : Spring gère la création et la destruction

---

## 14. Annotations clés à connaître

### Entités JPA
| Annotation | Rôle |
|------------|------|
| `@Entity` | Classe = table en base |
| `@Table(name = "...")` | Nom de la table |
| `@Id` | Clé primaire |
| `@GeneratedValue(strategy = IDENTITY)` | Auto-incrément |
| `@Column(nullable, unique)` | Contraintes de colonne |
| `@OneToOne`, `@ManyToOne`, `@OneToMany` | Relations |
| `@JoinColumn` | Colonne de clé étrangère |
| `@PrePersist` | Code exécuté avant insertion |
| `@UniqueConstraint` | Contrainte d'unicité composite |

### Controllers REST
| Annotation | Rôle |
|------------|------|
| `@RestController` | Controller qui retourne du JSON |
| `@RequestMapping("/api/...")` | URL de base |
| `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` | Méthodes HTTP |
| `@PathVariable` | Paramètre dans l'URL |
| `@RequestBody` | Corps de la requête (JSON → objet) |
| `@Valid` | Active la validation |

### Validation
| Annotation | Rôle |
|------------|------|
| `@NotBlank` | Non vide (String) |
| `@NotNull` | Non null |
| `@Min(0)`, `@Max(20)` | Valeur min/max |
| `@Email` | Format email |
| `@Size(min, max)` | Taille de chaîne |
| `@Positive` | > 0 |

### Sécurité
| Annotation | Rôle |
|------------|------|
| `@Configuration` | Classe de configuration |
| `@Bean` | Méthode qui retourne un composant Spring |
| `@Component` | Bean auto-détecté |

### Swagger
| Annotation | Rôle |
|------------|------|
| `@Tag(name = "...")` | Catégorie d'endpoints |
| `@Operation(summary = "...")` | Description d'un endpoint |
| `@ApiResponse(responseCode = "...")` | Réponse possible |
| `@SecurityRequirement` | Exige l'authentification |

---

## 15. Questions d'examen possibles

### Questions de cours

1. **Qu'est-ce qu'une API REST ?**
   → Un style d'architecture web qui utilise les méthodes HTTP (GET, POST, PUT, DELETE) pour manipuler des ressources identifiées par des URLs.

2. **Quelle est la différence entre @Controller et @RestController ?**
   → `@RestController` = `@Controller` + `@ResponseBody`. Il retourne directement du JSON au lieu de chercher une vue HTML.

3. **À quoi sert un DTO ?**
   → À transporter les données entre le client et le serveur sans exposer la structure interne de la base de données. Il permet de contrôler quels champs sont visibles et d'ajouter de la validation.

4. **Pourquoi utiliser JWT plutôt que les sessions ?**
   → JWT est stateless (pas d'état serveur), plus adapté aux API REST, facile à scaler, et fonctionne bien avec les clients mobiles ou SPA.

5. **Qu'est-ce que HATEOAS ?**
   → Un principe REST qui consiste à inclure des liens de navigation dans les réponses, permettant au client de découvrir les actions disponibles sans connaître les URLs à l'avance.

6. **Quelle est la différence entre JPA et Hibernate ?**
   → JPA est une spécification (un standard), Hibernate est une implémentation de cette spécification. Spring Boot utilise Hibernate par défaut.

7. **À quoi sert le `SecurityFilterChain` ?**
   → C'est la chaîne de filtres de sécurité Spring qui intercepte chaque requête HTTP pour vérifier l'authentification et l'autorisation avant qu'elle n'atteigne le controller.

8. **Qu'est-ce que l'injection de dépendances ?**
   → Un mécanisme où Spring crée les objets et les fournit automatiquement là où ils sont nécessaires (via le constructeur), au lieu de les créer manuellement avec `new`.

### Questions pratiques

9. **Comment ajouter un nouvel endpoint ?**
   → 1) Créer/modifier le DTO, 2) Ajouter une méthode dans le Repository si nécessaire, 3) Ajouter la logique dans le Service, 4) Ajouter la méthode dans le Controller avec les annotations appropriées, 5) Configurer l'accès dans SecurityConfig.

10. **Comment protéger un endpoint pour qu'il ne soit accessible qu'aux ADMIN ?**
    → Dans `SecurityConfig` : `.requestMatchers("/api/monendpoint/**").hasRole("ADMIN")`

11. **Comment créer une requête personnalisée ?**
    → Soit par convention de nommage (`findByNomAndPrenom`), soit avec `@Query("SELECT ... FROM ... WHERE ...")`.

12. **Comment fonctionne la validation dans Spring Boot ?**
    → 1) Annoter les champs du DTO (@NotBlank, @Min...), 2) Ajouter `@Valid` devant `@RequestBody` dans le controller, 3) Les erreurs sont interceptées par `@RestControllerAdvice`.

13. **Quel est le flux complet d'une requête authentifiée ?**
    → Client envoie `Authorization: Bearer <token>` → JwtAuthenticationFilter valide le token → Extrait email/rôle → Met dans SecurityContext → SecurityConfig vérifie le rôle → Controller reçoit la requête → Service exécute la logique → Repository accède à la BDD → Réponse JSON retournée.

14. **Comment fonctionne l'agrégation (statistiques) dans le projet ?**
    → Le `CoursService.obtenirStatistiques()` récupère les inscriptions et notes du cours, puis calcule en Java : le nombre d'étudiants, la moyenne, le max et le min des notes. Le résultat est retourné dans un `StatistiquesCoursDTO`.
