package com.example.miniprojetgestionscolaire.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professeurs")
public class Professeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "utilisateur_id",
            referencedColumnName = "id",
            nullable = false,
            unique = true
    )
    private Utilisateur utilisateur;

    @Column(nullable = false)
    private String specialite;

    @Column(nullable = false)
    private String classeEnseignee;
    public Professeur() {}

    public Professeur(Utilisateur utilisateur, String specialite, String classeEnseignee) {
        this.utilisateur = utilisateur;
        this.specialite = specialite;
        this.classeEnseignee = classeEnseignee;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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
}
