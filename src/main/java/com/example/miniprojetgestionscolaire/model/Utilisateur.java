package com.example.miniprojetgestionscolaire.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "utilisateurs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "unique_email")
        }
)
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    private String role;

    public Utilisateur() {}

    public Utilisateur(String nom,String email, String motDePasse, String role) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}