package com.example.miniprojetgestionscolaire.controller;

import com.example.miniprojetgestionscolaire.dto.EtudiantDTO;
import com.example.miniprojetgestionscolaire.dto.ResumeEtudiantDTO;
import com.example.miniprojetgestionscolaire.service.EtudiantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/etudiants")
@Tag(name = "Etudiants", description = "Gestion des étudiants")
@SecurityRequirement(name = "Bearer Authentication")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @Operation(
            summary = "Liste tous les étudiants",
            description = "Retourne la liste complète des étudiants—ADMIN requis",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
                    @ApiResponse(responseCode = "401", description = "Token manquant ou invalide"),
                    @ApiResponse(responseCode = "403", description = "Accès refusé")
            }
    )
    @GetMapping
    public ResponseEntity<List<EtudiantDTO>> findAll() {
        return ResponseEntity.ok(etudiantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtudiantDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(etudiantService.findById(id));
    }

    @GetMapping("/{id}/infos")
    public ResponseEntity<ResumeEtudiantDTO> getInfos(@PathVariable Long id) {
        return ResponseEntity.ok(etudiantService.obtenirResume(id));
    }


    @Operation(
            summary = "Créer un étudiant",
            description = "Crée un profil étudiant lié à un utilisateur existant. Le numéro étudiant est généré automatiquement.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EtudiantDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemple création étudiant",
                                    value = """
                {
                    "utilisateurId": 4,
                    "classe": "L3",
                    "filiere": "Informatique",
                    "anneeInscription": 2025
                }
                """
                            )
                    )
            )
    )
    @PostMapping
    public ResponseEntity<EtudiantDTO> creer(@Valid @RequestBody EtudiantDTO dto) {
        return ResponseEntity
                .status(201)
                .body(etudiantService.creer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EtudiantDTO> modifier(
            @PathVariable Long id,
            @Valid @RequestBody EtudiantDTO dto) {
        return ResponseEntity.ok(etudiantService.modifier(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> supprimer(@PathVariable Long id) {
        etudiantService.supprimer(id);
        return ResponseEntity.ok(Map.of("message", "Etudiant supprimé avec succès"));
    }
}
