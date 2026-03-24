package com.example.miniprojetgestionscolaire.model;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private Professeur professeur;

    @Column(nullable = false)
    private double note;

    private String commentaire;

    @Column(nullable = false)
    private LocalDateTime dateNote;

    @PrePersist
    public void avantSauvegarde() {
        this.dateNote = LocalDateTime.now();
    }

    public Note() {}

    public Note(Etudiant etudiant, Professeur professeur,
                double note, String commentaire) {
        this.etudiant = etudiant;
        this.professeur = professeur;
        this.note = note;
        this.commentaire = commentaire;
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

    public Professeur getProfesseur() {
        return professeur;
    }
    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public double getNote() {
        return note;
    }
    public void setNote(double note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDateTime getDateNote() {
        return dateNote;
    }
    public void setDateNote(LocalDateTime dateNote) {
        this.dateNote = dateNote;
    }
}
