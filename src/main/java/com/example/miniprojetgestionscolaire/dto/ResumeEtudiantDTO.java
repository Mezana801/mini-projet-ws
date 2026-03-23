package com.example.miniprojetgestionscolaire.dto;

import java.util.List;

public class ResumeEtudiantDTO {
    private Long id;
    private String nom;
    private String email;
    private String classe;
    private String filiere;
    private String numeroEtudiant;
    private int anneeInscription;
    private List<String> listeCours;
    private double moyenneGenerale;
    private int totalCredits;
    private int nombreCours;

    public ResumeEtudiantDTO() {}

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

    public List<String> getListeCours() {
        return listeCours;
    }

    public void setListeCours(List<String> listeCours) {
        this.listeCours = listeCours;
    }

    public double getMoyenneGenerale() {
        return moyenneGenerale;
    }

    public void setMoyenneGenerale(double moyenneGenerale) {
        this.moyenneGenerale = moyenneGenerale;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public int getNombreCours() {
        return nombreCours;
    }

    public void setNombreCours(int nombreCours) {
        this.nombreCours = nombreCours;
    }
}
