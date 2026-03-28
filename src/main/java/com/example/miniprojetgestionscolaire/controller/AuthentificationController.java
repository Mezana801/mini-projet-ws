package com.example.miniprojetgestionscolaire.controller;

import com.example.miniprojetgestionscolaire.dto.ConnexionDTO;
import com.example.miniprojetgestionscolaire.dto.InscriptionUtilisateurDTO;
import com.example.miniprojetgestionscolaire.model.Utilisateur;
import com.example.miniprojetgestionscolaire.service.AuthentificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {

    @Autowired
    private AuthentificationService authentificationService;
    @Operation(summary = "Authentification utilisateur")
    @PostMapping("/connexion")
    public ResponseEntity<EntityModel<Map<String, String>>> connecter(
            @Valid @RequestBody ConnexionDTO dto) {

        String token = authentificationService.connecter(
                dto.getEmail(),
                dto.getMotDePasse()
        );

        if (token == null) {
            return ResponseEntity.status(401).body(
                    EntityModel.of(Map.of("erreur",
                            "Mot de passe incorrect ou email incorrect"))
            );
        }

        EntityModel<Map<String, String>> reponse = EntityModel.of(
                Map.of("token", token),

                linkTo(methodOn(AuthentificationController.class)
                        .inscrire(null)).withRel("inscription"),
                linkTo(methodOn(EtudiantController.class)
                        .findAll()).withRel("etudiants")
        );
        return ResponseEntity.ok(reponse);
    }
    @Operation(summary = "Inscription utilisateur")
    @PostMapping("/inscription")
    public ResponseEntity<EntityModel<Map<String, String>>> inscrire(
            @Valid @RequestBody InscriptionUtilisateurDTO dto) {

        Utilisateur nouvelUtilisateur = authentificationService.inscrire(dto);

        EntityModel<Map<String, String>> reponse = EntityModel.of(
                Map.of(
                        "message", "Compte créé avec succès",
                        "nom", nouvelUtilisateur.getNom(),
                        "email", nouvelUtilisateur.getEmail(),
                        "role", nouvelUtilisateur.getRole()
                ),
                linkTo(methodOn(AuthentificationController.class)
                        .connecter(null)).withRel("connexion")
        );

        return ResponseEntity.status(201).body(reponse);
    }
}
