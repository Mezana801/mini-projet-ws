package com.example.miniprojetgestionscolaire.service;

import com.example.miniprojetgestionscolaire.dto.CoursDTO;
import com.example.miniprojetgestionscolaire.dto.StatistiquesCoursDTO;
import com.example.miniprojetgestionscolaire.model.Cours;
import com.example.miniprojetgestionscolaire.model.Inscription;
import com.example.miniprojetgestionscolaire.model.Note;
import com.example.miniprojetgestionscolaire.model.Professeur;
import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.repository.CoursRepository;
import com.example.miniprojetgestionscolaire.repository.InscriptionRepository;
import com.example.miniprojetgestionscolaire.repository.NoteRepository;
import com.example.miniprojetgestionscolaire.repository.ProfesseurRepository;
import com.example.miniprojetgestionscolaire.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoursService {

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private NoteRepository noteRepository;

    private CoursDTO coursToDto(Cours cours) {
        CoursDTO dto = new CoursDTO();
        dto.setId(cours.getId());
        dto.setNom(cours.getNom());
        dto.setDescription(cours.getDescription());
        dto.setCredits(cours.getCredits());
        dto.setProfesseurId(cours.getProfesseur().getId());
        dto.setNomProfesseur(cours.getProfesseur().getUtilisateur().getNom());
        return dto;
    }

    public List<CoursDTO> findAll() {
        return coursRepository.findAll()
                .stream()
                .map(this::coursToDto)
                .collect(Collectors.toList());
    }

    public CoursDTO findById(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours introuvable avec l'id : " + id));
        return coursToDto(cours);
    }

    public CoursDTO creer(CoursDTO dto) {
        Professeur professeur;

        if (dto.getProfesseurId() != null) {
            professeur = professeurRepository.findById(dto.getProfesseurId())
                    .orElseThrow(() -> new RuntimeException("Professeur introuvable"));
        } else {
            String email = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
            professeur = professeurRepository.findByUtilisateurId(utilisateur.getId())
                    .orElseThrow(() -> new RuntimeException("Profil professeur introuvable"));
        }

        Cours cours = new Cours(
                dto.getNom(),
                dto.getDescription(),
                dto.getCredits(),
                professeur
        );

        return coursToDto(coursRepository.save(cours));
    }

    public CoursDTO modifier(Long id, CoursDTO dto) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours introuvable avec l'id : " + id));

        cours.setNom(dto.getNom());
        cours.setDescription(dto.getDescription());
        cours.setCredits(dto.getCredits());

        if (dto.getProfesseurId() != null) {
            Professeur professeur = professeurRepository.findById(dto.getProfesseurId())
                    .orElseThrow(() -> new RuntimeException("Professeur introuvable"));
            cours.setProfesseur(professeur);
        }

        return coursToDto(coursRepository.save(cours));
    }

    public void supprimer(Long id) {
        if (!coursRepository.existsById(id)) {
            throw new RuntimeException("Cours introuvable avec l'id : " + id);
        }
        coursRepository.deleteById(id);
    }

    public StatistiquesCoursDTO obtenirStatistiques(Long coursId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours introuvable avec l'id : " + coursId));

        List<Inscription> inscriptions = inscriptionRepository.findByCoursId(coursId);

        List<Long> etudiantIds = inscriptions.stream()
                .map(i -> i.getEtudiant().getId())
                .collect(Collectors.toList());

        List<Note> notes = etudiantIds.stream()
                .flatMap(etudiantId -> noteRepository.findByEtudiantIdAndProfesseurId(
                        etudiantId, cours.getProfesseur().getId()).stream())
                .collect(Collectors.toList());

        StatistiquesCoursDTO stats = new StatistiquesCoursDTO();
        stats.setId(cours.getId());
        stats.setNomCours(cours.getNom());
        stats.setProfesseur(cours.getProfesseur().getUtilisateur().getNom());
        stats.setNombreEtudiants(inscriptions.size());

        if (!notes.isEmpty()) {
            double moyenne = notes.stream()
                    .mapToDouble(Note::getNote)
                    .average()
                    .orElse(0.0);
            double max = notes.stream()
                    .mapToDouble(Note::getNote)
                    .max()
                    .orElse(0.0);
            double min = notes.stream()
                    .mapToDouble(Note::getNote)
                    .min()
                    .orElse(0.0);

            stats.setMoyenneClasse(Math.round(moyenne * 10.0) / 10.0);
            stats.setMeilleureNote(max);
            stats.setNoteMinimale(min);
        } else {
            stats.setMoyenneClasse(0.0);
            stats.setMeilleureNote(0.0);
            stats.setNoteMinimale(0.0);
        }

        return stats;
    }
}
