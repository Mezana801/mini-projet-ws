package com.example.miniprojetgestionscolaire.config;

import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ChargeurDonnees implements CommandLineRunner {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

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

            utilisateurRepository.save(new Utilisateur(
                    "David","etudiant.david@school.com", "etu123", "ETUDIANT"
            ));

            utilisateurRepository.save(new Utilisateur(
                    "Marie","etudiant.marie@school.com", "etu456", "ETUDIANT"
            ));

        }
    }
}