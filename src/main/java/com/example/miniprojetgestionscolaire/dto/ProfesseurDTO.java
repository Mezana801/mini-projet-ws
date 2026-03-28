package com.example.miniprojetgestionscolaire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProfesseurDTO {

    private Long id;
    private String nom;
    private String email;

    @NotBlank(message = "La spécialité est obligatoire")
    private String specialite;

    @NotBlank(message = "La classe enseignée est obligatoire")
    private String classeEnseignee;

    private Long utilisateurId;

    public ProfesseurDTO() {}

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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialite() {
        return specialite;
    }
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getClasseEnseignee() {
        return classeEnseignee;
    }
    public void setClasseEnseignee(String classeEnseignee) {
        this.classeEnseignee = classeEnseignee;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }
    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }
}
