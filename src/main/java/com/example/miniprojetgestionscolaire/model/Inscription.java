package com.example.miniprojetgestionscolaire.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions")
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @Column(nullable = false)
    private LocalDateTime dateInscription;

    @PrePersist
    public void avantSauvegarde() {
        this.dateInscription = LocalDateTime.now();
    }

    public Inscription() {}

    public Inscription(Etudiant etudiant, Cours cours) {
        this.etudiant = etudiant;
        this.cours = cours;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Cours getCours() {
        return cours;
    }
    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }
    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }
}
