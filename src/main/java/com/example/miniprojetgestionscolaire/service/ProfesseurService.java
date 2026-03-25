package com.example.miniprojetgestionscolaire.service;

import com.example.miniprojetgestionscolaire.dto.ProfesseurDTO;
import com.example.miniprojetgestionscolaire.model.Professeur;
import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.repository.ProfesseurRepository;
import com.example.miniprojetgestionscolaire.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesseurService {

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private ProfesseurDTO professeurToDto(Professeur professeur) {
        ProfesseurDTO dto = new ProfesseurDTO();
        dto.setId(professeur.getId());
        dto.setNom(professeur.getUtilisateur().getNom());
        dto.setEmail(professeur.getUtilisateur().getEmail());
        dto.setSpecialite(professeur.getSpecialite());
        dto.setClasseEnseignee(professeur.getClasseEnseignee());
        dto.setUtilisateurId(professeur.getUtilisateur().getId());
        return dto;
    }

    public List<ProfesseurDTO> findAll() {
        return professeurRepository.findAll()
                .stream()
                .map(this::professeurToDto)
                .collect(Collectors.toList());
    }

    public ProfesseurDTO findById(Long id) {
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professeur introuvable avec l'id : " + id));
        return professeurToDto(professeur);
    }

    public ProfesseurDTO creer(ProfesseurDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!utilisateur.getRole().equals("PROFESSEUR")) {
            throw new RuntimeException("L'utilisateur doit avoir le rôle PROFESSEUR");
        }

        if (professeurRepository.findByUtilisateurId(utilisateur.getId()).isPresent()) {
            throw new RuntimeException("Cet utilisateur a déjà un profil professeur");
        }

        Professeur professeur = new Professeur(
                utilisateur,
                dto.getSpecialite(),
                dto.getClasseEnseignee()
        );

        return professeurToDto(professeurRepository.save(professeur));
    }

    public ProfesseurDTO modifier(Long id, ProfesseurDTO dto) {
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professeur introuvable avec l'id : " + id));

        professeur.setSpecialite(dto.getSpecialite());
        professeur.setClasseEnseignee(dto.getClasseEnseignee());

        return professeurToDto(professeurRepository.save(professeur));
    }

    public void supprimer(Long id) {
        if (!professeurRepository.existsById(id)) {
            throw new RuntimeException("Professeur introuvable avec l'id : " + id);
        }
        professeurRepository.deleteById(id);
    }
}
