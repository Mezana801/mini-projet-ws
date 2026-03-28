package com.example.miniprojetgestionscolaire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CoursDTO {

    private Long id;

    @NotBlank(message = "Le nom du cours est obligatoire")
    private String nom;

    private String description;

    @NotNull(message = "Le nombre de crédits est obligatoire")
    @Positive(message = "Les crédits doivent être positifs")
    private int credits;

    private Long professeurId;
    private String nomProfesseur;

    public CoursDTO() {}

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

    public Long getProfesseurId() {
        return professeurId;
    }
    public void setProfesseurId(Long professeurId) {
        this.professeurId = professeurId;
    }

    public String getNomProfesseur() {
        return nomProfesseur;
    }
    public void setNomProfesseur(String nomProfesseur) {
        this.nomProfesseur = nomProfesseur;
    }
}
