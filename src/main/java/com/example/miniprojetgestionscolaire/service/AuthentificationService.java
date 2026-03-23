package com.example.miniprojetgestionscolaire.service;

import com.example.miniprojetgestionscolaire.dto.InscriptionUtilisateurDTO;
import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthentificationService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtService jwtService;

    public String connecter(String email, String motDePasse) {

        Optional<Utilisateur> utilisateur =
                utilisateurRepository.findByEmailAndMotDePasse(email, motDePasse);

        if (utilisateur.isEmpty()) {
            return null;
        }

        return jwtService.genererToken(
                utilisateur.get().getEmail(),
                utilisateur.get().getRole()
        );
    }

    public Utilisateur inscrire(InscriptionUtilisateurDTO dto) {

        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        Utilisateur nouvelUtilisateur = new Utilisateur(
                dto.getNom(),
                dto.getEmail(),
                dto.getMotDePasse(),
                "ETUDIANT"
        );

        return utilisateurRepository.save(nouvelUtilisateur);
    }
}
