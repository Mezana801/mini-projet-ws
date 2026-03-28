package com.example.miniprojetgestionscolaire.controller;

import com.example.miniprojetgestionscolaire.dto.ProfesseurDTO;
import com.example.miniprojetgestionscolaire.service.ProfesseurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professeurs")
@Tag(name = "Professeurs", description = "Gestion des professeurs")
@SecurityRequirement(name = "Bearer Authentication")
public class ProfesseurController {

    @Autowired
    private ProfesseurService professeurService;

    private EntityModel<ProfesseurDTO> toModel(ProfesseurDTO dto) {
        EntityModel<ProfesseurDTO> model = EntityModel.of(dto);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesseurController.class).findById(dto.getId())).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesseurController.class).findAll()).withRel("professeurs"));
        return model;
    }

    @Operation(
            summary = "Liste tous les professeurs",
            description = "Retourne la liste complète des professeurs",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
                    @ApiResponse(responseCode = "401", description = "Token manquant ou invalide")
            }
    )
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProfesseurDTO>>> findAll() {
        List<EntityModel<ProfesseurDTO>> professeurs = professeurService.findAll()
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ProfesseurDTO>> collection = CollectionModel.of(professeurs);
        collection.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesseurController.class).findAll()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(
            summary = "Détail d'un professeur",
            description = "Retourne les informations d'un professeur par son ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProfesseurDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(professeurService.findById(id)));
    }

    @Operation(
            summary = "Créer un professeur",
            description = "Crée un profil professeur lié à un utilisateur existant—ADMIN requis",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProfesseurDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemple création professeur",
                                    value = """
                {
                    "utilisateurId": 2,
                    "specialite": "Informatique",
                    "classeEnseignee": "L3"
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Professeur créé avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides"),
                    @ApiResponse(responseCode = "403", description = "Accès refusé—ADMIN requis")
            }
    )
    @PostMapping
    public ResponseEntity<EntityModel<ProfesseurDTO>> creer(@Valid @RequestBody ProfesseurDTO dto) {
        return ResponseEntity.status(201).body(toModel(professeurService.creer(dto)));
    }

    @Operation(
            summary = "Modifier un professeur",
            description = "Met à jour la spécialité et la classe enseignée d'un professeur",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProfesseurDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemple modification professeur",
                                    value = """
                {
                    "specialite": "Mathématiques",
                    "classeEnseignee": "M1"
                }
                """
                            )
                    )
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProfesseurDTO>> modifier(
            @PathVariable Long id,
            @Valid @RequestBody ProfesseurDTO dto) {
        return ResponseEntity.ok(toModel(professeurService.modifier(id, dto)));
    }

    @Operation(
            summary = "Supprimer un professeur",
            description = "Supprime un professeur—ADMIN requis",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Professeur supprimé avec succès"),
                    @ApiResponse(responseCode = "403", description = "Accès refusé—ADMIN requis")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> supprimer(@PathVariable Long id) {
        professeurService.supprimer(id);
        return ResponseEntity.ok(Map.of("message", "Professeur supprimé avec succès"));
    }
}
