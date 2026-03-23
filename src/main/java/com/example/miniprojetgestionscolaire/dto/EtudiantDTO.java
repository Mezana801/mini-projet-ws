package com.example.miniprojetgestionscolaire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EtudiantDTO {

    private Long id;
    private String nom;
    private String email;

    @NotBlank(message = "La classe est obligatoire")
    private String classe;

    @NotBlank(message = "La filière est obligatoire")
    private String filiere;


    private String numeroEtudiant;

    @NotNull(message = "L'année d'inscription est obligatoire")
    @Positive(message = "L'année doit être un nombre positif")
    private int anneeInscription;


    //@NotNull(message = "L'identifiant utilisateur est obligatoire")
    private Long utilisateurId;

    public EtudiantDTO() {}

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

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public int getAnneeInscription() {
        return anneeInscription;
    }

    public void setAnneeInscription(int anneeInscription) {
        this.anneeInscription = anneeInscription;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }
}
