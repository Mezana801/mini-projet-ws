package com.example.miniprojetgestionscolaire.service;


import com.example.miniprojetgestionscolaire.dto.EtudiantDTO;
import com.example.miniprojetgestionscolaire.dto.ResumeEtudiantDTO;
import com.example.miniprojetgestionscolaire.model.Etudiant;
import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.repository.EtudiantRepository;
import com.example.miniprojetgestionscolaire.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;


    private String genererNumeroEtudiant() {
        long total = etudiantRepository.count();

        return String.format("ETU%06d", total + 1);
    }
    private EtudiantDTO etudiantToDto(Etudiant etudiant) {
        EtudiantDTO dto = new EtudiantDTO();
        dto.setId(etudiant.getId());
        dto.setNom(etudiant.getUtilisateur().getNom());
        dto.setEmail(etudiant.getUtilisateur().getEmail());
        dto.setClasse(etudiant.getClasse());
        dto.setFiliere(etudiant.getFiliere());
        dto.setNumeroEtudiant(etudiant.getNumeroEtudiant());
        dto.setAnneeInscription(etudiant.getAnneeInscription());
        dto.setUtilisateurId(etudiant.getUtilisateur().getId());
        return dto;
    }

    public List<EtudiantDTO> findAll() {
        return etudiantRepository.findAll()
                .stream()
                .map(this::etudiantToDto)
                .collect(Collectors.toList());
    }


    public EtudiantDTO findById(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable avec l'id : " + id));
        return etudiantToDto(etudiant);
    }


    public EtudiantDTO creer(EtudiantDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!utilisateur.getRole().equals("ETUDIANT")) {
            throw new RuntimeException("L'utilisateur doit avoir le rôle ETUDIANT");
        }

        if (etudiantRepository.existsByNumeroEtudiant(dto.getNumeroEtudiant())) {
            throw new RuntimeException("Ce numéro étudiant est déjà utilisé");
        }
        if (etudiantRepository.findByUtilisateurId(utilisateur.getId()).isPresent()) {
            throw new RuntimeException("Cet utilisateur a déjà un profil étudiant");
        }

        String numeroEtudiant = genererNumeroEtudiant();

        Etudiant etudiant = new Etudiant(
                utilisateur,
                dto.getClasse(),
                dto.getFiliere(),
                numeroEtudiant,
                dto.getAnneeInscription()
        );

        return etudiantToDto(etudiantRepository.save(etudiant));
    }

    public EtudiantDTO modifier(Long id, EtudiantDTO dto) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable avec l'id : " + id));

        etudiant.setClasse(dto.getClasse());
        etudiant.setFiliere(dto.getFiliere());
        etudiant.setAnneeInscription(dto.getAnneeInscription());

        return etudiantToDto(etudiantRepository.save(etudiant));
    }

    public void supprimer(Long id) {
        if (!etudiantRepository.existsById(id)) {
            throw new RuntimeException("Etudiant introuvable avec l'id : " + id);
        }
        etudiantRepository.deleteById(id);
    }

    public ResumeEtudiantDTO obtenirResume(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable avec l'id : " + id));

        ResumeEtudiantDTO resume = new ResumeEtudiantDTO();
        resume.setId(etudiant.getId());
        resume.setNom(etudiant.getUtilisateur().getNom());
        resume.setEmail(etudiant.getUtilisateur().getEmail());
        resume.setClasse(etudiant.getClasse());
        resume.setFiliere(etudiant.getFiliere());
        resume.setNumeroEtudiant(etudiant.getNumeroEtudiant());
        resume.setAnneeInscription(etudiant.getAnneeInscription());
        resume.setListeCours(new ArrayList<>());
        resume.setMoyenneGenerale(0.0);
        resume.setTotalCredits(0);
        resume.setNombreCours(0);

        return resume;
    }
}
