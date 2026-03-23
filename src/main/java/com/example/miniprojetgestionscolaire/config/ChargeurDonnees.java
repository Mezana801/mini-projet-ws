package com.example.miniprojetgestionscolaire.config;

import com.example.miniprojetgestionscolaire.dto.EtudiantDTO;
import com.example.miniprojetgestionscolaire.model.Etudiant;
import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.repository.EtudiantRepository;
import com.example.miniprojetgestionscolaire.repository.UtilisateurRepository;
import com.example.miniprojetgestionscolaire.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ChargeurDonnees implements CommandLineRunner {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EtudiantService etudiantService;

    @Override
    public void run(String... args) throws Exception {

        if (utilisateurRepository.count() == 0) {

            utilisateurRepository.save(new Utilisateur(
                    "Jean","admin@school.com", "admin123", "ADMIN"
            ));

            utilisateurRepository.save(new Utilisateur(
                    "Mika","prof.mika@school.com", "prof123", "PROFESSEUR"
            ));

            utilisateurRepository.save(new Utilisateur(
                    "Michel","prof.michel@school.com", "prof456", "PROFESSEUR"
            ));

           Utilisateur david= utilisateurRepository.save(new Utilisateur(
                    "David","etudiant.david@school.com", "etu123", "ETUDIANT"
            ));

           Utilisateur marie= utilisateurRepository.save(new Utilisateur(
                    "Marie","etudiant.marie@school.com", "etu456", "ETUDIANT"
            ));

            EtudiantDTO dto1 = new EtudiantDTO();
            dto1.setUtilisateurId(david.getId());
            dto1.setClasse("L3");
            dto1.setFiliere("Informatique");
            dto1.setAnneeInscription(2026);
            etudiantService.creer(dto1);

            EtudiantDTO dto2 = new EtudiantDTO();
            dto2.setUtilisateurId(marie.getId());
            dto2.setClasse("M1");
            dto2.setFiliere("Mathématiques");
            dto2.setAnneeInscription(2026);
            etudiantService.creer(dto2);

        }
    }
}