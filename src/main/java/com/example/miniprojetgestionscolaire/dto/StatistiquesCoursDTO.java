package com.example.miniprojetgestionscolaire.dto;

public class StatistiquesCoursDTO {

    private Long id;
    private String nomCours;
    private String professeur;
    private int nombreEtudiants;
    private double moyenneClasse;
    private double meilleureNote;
    private double noteMinimale;

    public StatistiquesCoursDTO() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCours() {
        return nomCours;
    }
    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    public String getProfesseur() {
        return professeur;
    }
    public void setProfesseur(String professeur) {
        this.professeur = professeur;
    }

    public int getNombreEtudiants() {
        return nombreEtudiants;
    }
    public void setNombreEtudiants(int nombreEtudiants) {
        this.nombreEtudiants = nombreEtudiants;
    }

    public double getMoyenneClasse() {
        return moyenneClasse;
    }
    public void setMoyenneClasse(double moyenneClasse) {
        this.moyenneClasse = moyenneClasse;
    }

    public double getMeilleureNote() {
        return meilleureNote;
    }
    public void setMeilleureNote(double meilleureNote) {
        this.meilleureNote = meilleureNote;
    }

    public double getNoteMinimale() {
        return noteMinimale;
    }
    public void setNoteMinimale(double noteMinimale) {
        this.noteMinimale = noteMinimale;
    }
}
