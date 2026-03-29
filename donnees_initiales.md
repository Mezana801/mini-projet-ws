# Données chargées au démarrage — Application locale
## Système de Gestion Scolaire — MBDS 2025-2026
**Spring Boot 3.4.3 | JDK 17**

Ces données sont insérées automatiquement au premier démarrage de l'application locale. Sur l'application déployée sur Render, ces données sont déjà présentes dans la base.

---

## Utilisateurs

| Utilisateur | Email | Mot de passe | Rôle |
|-------------|-------|-------------|------|
| Jean | admin@school.com | admin123 | ADMIN |
| Mika | prof.mika@school.com | prof123 | PROFESSEUR |
| Michel | prof.michel@school.com | prof456 | PROFESSEUR |
| David | etudiant.david@school.com | etu123 | ETUDIANT |
| Marie | etudiant.marie@school.com | etu456 | ETUDIANT |

---

## Professeurs

| Professeur | Spécialité | Classe enseignée |
|------------|-----------|-----------------|
| Mika | Informatique | L3 |
| Michel | Mathématiques | M1 |

---

## Etudiants

| Etudiant | Classe | Filière | Numéro étudiant |
|----------|--------|---------|----------------|
| David | L3 | Informatique | ETU000001 |
| Marie | M1 | Mathématiques | ETU000002 |

---

## Cours

| Cours | Crédits | Professeur responsable |
|-------|---------|----------------------|
| Informatique | 3 | Mika |
| Mathématiques | 3 | Michel |
| Physique | 3 | Michel |

---

## Remarque

Sur l'application locale, ces données sont insérées automatiquement par le `ChargeurDonnees` au démarrage uniquement si la base est vide. Sur l'application déployée sur Render, ces données sont déjà présentes et persistées dans la base H2 fichier.
