package com.example.miniprojetgestionscolaire.config;

import com.example.miniprojetgestionscolaire.dto.EtudiantDTO;
import com.example.miniprojetgestionscolaire.model.Cours;
import com.example.miniprojetgestionscolaire.model.Etudiant;
import com.example.miniprojetgestionscolaire.model.Inscription;
import com.example.miniprojetgestionscolaire.model.Professeur;
import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.repository.CoursRepository;
import com.example.miniprojetgestionscolaire.repository.EtudiantRepository;
import com.example.miniprojetgestionscolaire.repository.InscriptionRepository;
import com.example.miniprojetgestionscolaire.repository.ProfesseurRepository;
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

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Override
    public void run(String... args) throws Exception {

        if (utilisateurRepository.count() == 0) {

            utilisateurRepository.save(new Utilisateur(
                    "Jean","admin@school.com", "admin123", "ADMIN"
            ));

           Utilisateur mika= utilisateurRepository.save(new Utilisateur(
                    "Mika","prof.mika@school.com", "prof123", "PROFESSEUR"
            ));

            Utilisateur michel=utilisateurRepository.save(new Utilisateur(
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

            Professeur profMika = professeurRepository.save(new Professeur(mika, "Informatique", "L3"));
            Professeur profMichel = professeurRepository.save(new Professeur(michel, "Mathématiques", "M1"));

            // Cours
            Cours coursInfo = coursRepository.save(new Cours("Informatique", "Cours d'informatique générale", 3, profMika));
            Cours coursMaths = coursRepository.save(new Cours("Mathématiques", "Cours de mathématiques appliquées", 3, profMichel));
            Cours coursPhysique = coursRepository.save(new Cours("Physique", "Cours de physique fondamentale", 3, profMichel));

            // Inscriptions
            Etudiant etudiantDavid = etudiantRepository.findByUtilisateurId(david.getId()).orElse(null);
            Etudiant etudiantMarie = etudiantRepository.findByUtilisateurId(marie.getId()).orElse(null);

            if (etudiantDavid != null) {
                inscriptionRepository.save(new Inscription(etudiantDavid, coursInfo));
                inscriptionRepository.save(new Inscription(etudiantDavid, coursMaths));
            }
            if (etudiantMarie != null) {
                inscriptionRepository.save(new Inscription(etudiantMarie, coursMaths));
                inscriptionRepository.save(new Inscription(etudiantMarie, coursPhysique));
            }

        }
    }
}