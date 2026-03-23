package com.example.miniprojetgestionscolaire.model;

import jakarta.persistence.*;

@Entity
@Table(name = "etudiants")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @Column(nullable = false)
    private String classe;

    @Column(nullable = false)
    private String filiere;

    @Column(nullable = false)
    private String numeroEtudiant;

    @Column(nullable = false)
    private int anneeInscription;

    public Etudiant() {}

    public Etudiant(Utilisateur utilisateur, String classe,
                    String filiere, String numeroEtudiant, int anneeInscription) {
        this.utilisateur = utilisateur;
        this.classe = classe;
        this.filiere = filiere;
        this.numeroEtudiant = numeroEtudiant;
        this.anneeInscription = anneeInscription;
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
}