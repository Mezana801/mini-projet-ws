package com.example.miniprojetgestionscolaire.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cours")
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;

    @Column(nullable = false)
    private int credits;

    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;

    public Cours() {}

    public Cours(String nom, String description, int credits, Professeur professeur) {
        this.nom = nom;
        this.description = description;
        this.credits = credits;
        this.professeur = professeur;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredits() {
        return credits;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Professeur getProfesseur() {
        return professeur;
    }
    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }
}
