package com.example.miniprojetgestionscolaire.service;



import com.example.miniprojetgestionscolaire.dto.NoteDTO;
import com.example.miniprojetgestionscolaire.model.Etudiant;
import com.example.miniprojetgestionscolaire.model.Note;
import com.example.miniprojetgestionscolaire.model.Professeur;
import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.repository.EtudiantRepository;
import com.example.miniprojetgestionscolaire.repository.NoteRepository;
import com.example.miniprojetgestionscolaire.repository.ProfesseurRepository;
import com.example.miniprojetgestionscolaire.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private NoteDTO noteToDto(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setEtudiantId(note.getEtudiant().getId());
        dto.setNomEtudiant(note.getEtudiant().getUtilisateur().getNom());
        dto.setClasseEtudiant(note.getEtudiant().getClasse());
        dto.setNomProfesseur(note.getProfesseur().getUtilisateur().getNom());
        dto.setScore(note.getNote());
        dto.setCommentaire(note.getCommentaire());
        dto.setDateNote(note.getDateNote().toString());
        return dto;
    }

    private Professeur getProfesseurConnecte() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return professeurRepository.findByUtilisateurId(utilisateur.getId())
                .orElseThrow(() -> new RuntimeException("Profil professeur introuvable"));
    }


    public NoteDTO ajouter(NoteDTO dto) {

        Professeur professeur = getProfesseurConnecte();

        Etudiant etudiant = etudiantRepository.findById(dto.getEtudiantId())
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable"));

        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_PROFESSEUR")) {
            if (!etudiant.getClasse().equals(professeur.getClasseEnseignee())) {
                throw new RuntimeException(
                        "Accès refusé, vous enseignez en " +
                                professeur.getClasseEnseignee() +
                                " et cet étudiant est en " +
                                etudiant.getClasse()
                );
            }
        }

        Note note = new Note(
                etudiant,
                professeur,
                dto.getScore(),
                dto.getCommentaire()
        );

        return noteToDto(noteRepository.save(note));
    }


    public NoteDTO modifier(Long id, NoteDTO dto) {

        Professeur professeur = getProfesseurConnecte();

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note introuvable avec l'id : " + id));

        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();


        if (role.equals("ROLE_PROFESSEUR")) {
            if (!note.getProfesseur().getId().equals(professeur.getId())) {
                throw new RuntimeException("Accès refusé, cette note ne vous appartient pas");
            }
        }



        note.setNote(dto.getScore());
        note.setCommentaire(dto.getCommentaire());

        return noteToDto(noteRepository.save(note));
    }


    public List<NoteDTO> obtenirNotesEtudiant(Long etudiantId) {

        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable"));

        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();


        if (role.equals("ROLE_ADMIN")) {
            return noteRepository.findByEtudiantId(etudiantId)
                    .stream()
                    .map(this::noteToDto)
                    .collect(Collectors.toList());
        }


        Professeur professeur = getProfesseurConnecte();

        if (!etudiant.getClasse().equals(professeur.getClasseEnseignee())) {
            throw new RuntimeException(
                    "Accès refusé, cet étudiant est en " +
                            etudiant.getClasse() +
                            ", vous enseignez en " +
                            professeur.getClasseEnseignee()
            );
        }

        return noteRepository.findByEtudiantIdAndProfesseurId(etudiantId, professeur.getId())
                .stream()
                .map(this::noteToDto)
                .collect(Collectors.toList());
    }

    public List<NoteDTO> obtenirToutesLesNotes() {

        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority(); // "ROLE_ADMIN" ou "ROLE_PROFESSEUR"

        if (role.equals("ROLE_ADMIN")) {
            return noteRepository.findAll()
                    .stream()
                    .map(this::noteToDto)
                    .collect(Collectors.toList());
        }

        if (role.equals("ROLE_PROFESSEUR")) {
            Professeur professeur = getProfesseurConnecte();

            return noteRepository.findByClasseEtudiant(professeur.getClasseEnseignee())
                    .stream()
                    .map(this::noteToDto)
                    .collect(Collectors.toList());
        }


        throw new RuntimeException("Accès refusé, vous n'avez pas les droits nécessaires");
    }
}
