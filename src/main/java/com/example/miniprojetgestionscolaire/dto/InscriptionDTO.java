package com.example.miniprojetgestionscolaire.dto;

import jakarta.validation.constraints.NotNull;

public class InscriptionDTO {

    private Long id;

    @NotNull(message = "L'identifiant de l'étudiant est obligatoire")
    private Long etudiantId;

    private String nomEtudiant;

    @NotNull(message = "L'identifiant du cours est obligatoire")
    private Long coursId;

    private String nomCours;
    private String dateInscription;

    public InscriptionDTO() {}

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

    public Long getCoursId() {
        return coursId;
    }
    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }

    public String getNomCours() {
        return nomCours;
    }
    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    public String getDateInscription() {
        return dateInscription;
    }
    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }
}
