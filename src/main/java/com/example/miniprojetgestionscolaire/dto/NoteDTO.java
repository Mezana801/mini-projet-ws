package com.example.miniprojetgestionscolaire.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class NoteDTO {

    private Long id;

    @NotNull(message = "L'identifiant de l'étudiant est obligatoire")
    private Long etudiantId;

    private String nomEtudiant;
    private String classeEtudiant;

    private String nomProfesseur;

    @NotNull(message = "La note est obligatoire")
    @Min(value = 0, message = "La note minimale est 0")
    @Max(value = 20, message = "La note maximale est 20")
    private double note;

    private String commentaire;
    private String dateNote;

    public NoteDTO() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }
    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public String getNomEtudiant() {
        return nomEtudiant;
    }
    public void setNomEtudiant(String nomEtudiant) {
        this.nomEtudiant = nomEtudiant;
    }

    public String getClasseEtudiant() {
        return classeEtudiant;
    }
    public void setClasseEtudiant(String classeEtudiant) {
        this.classeEtudiant = classeEtudiant;
    }

    public String getNomProfesseur() {
        return nomProfesseur;
    }
    public void setNomProfesseur(String nomProfesseur) {
        this.nomProfesseur = nomProfesseur;
    }

    public double getScore() {
        return note;
    }
    public void setScore(double score) {
        this.note = score;
    }

    public String getCommentaire() {
        return commentaire;
    }
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getDateNote() {
        return dateNote;
    }
    public void setDateNote(String dateNote) {
        this.dateNote = dateNote;
    }
}