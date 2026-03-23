package com.example.miniprojetgestionscolaire.controller;

import com.example.miniprojetgestionscolaire.dto.ConnexionDTO;
import com.example.miniprojetgestionscolaire.dto.InscriptionUtilisateurDTO;
import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.service.AuthentificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {

    @Autowired
    private AuthentificationService authentificationService;

    @PostMapping("/connexion")
    public ResponseEntity<Map<String, String>> connecter(
            @Valid @RequestBody ConnexionDTO dto) {

        String token = authentificationService.connecter(
                dto.getEmail(),
                dto.getMotDePasse()
        );

        if (token == null) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("erreur", "Email ou mot de passe incorrect"));
        }

        return ResponseEntity.ok(Map.of("token", token));
    }
    @PostMapping("/inscription")
    public ResponseEntity<Map<String, String>> inscrire(
            @Valid @RequestBody InscriptionUtilisateurDTO dto) {

        Utilisateur nouvelUtilisateur = authentificationService.inscrire(dto);

        return ResponseEntity
                .status(201)
                .body(Map.of(
                        "message", "Compte créé avec succès",
                        "nom", nouvelUtilisateur.getNom(),
                        "email", nouvelUtilisateur.getEmail(),
                        "role", nouvelUtilisateur.getRole()
                ));
    }
}
