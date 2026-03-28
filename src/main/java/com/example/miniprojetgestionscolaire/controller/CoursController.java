package com.example.miniprojetgestionscolaire.controller;

import com.example.miniprojetgestionscolaire.dto.CoursDTO;
import com.example.miniprojetgestionscolaire.dto.StatistiquesCoursDTO;
import com.example.miniprojetgestionscolaire.service.CoursService;
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
@RequestMapping("/api/cours")
@Tag(name = "Cours", description = "Gestion des cours")
public class CoursController {

    @Autowired
    private CoursService coursService;

    private EntityModel<CoursDTO> toModel(CoursDTO dto) {
        EntityModel<CoursDTO> model = EntityModel.of(dto);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoursController.class).findById(dto.getId())).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoursController.class).findAll()).withRel("cours"));
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoursController.class).obtenirStatistiques(dto.getId())).withRel("statistiques"));
        if (dto.getProfesseurId() != null) {
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProfesseurController.class).findById(dto.getProfesseurId())).withRel("professeur"));
        }
        return model;
    }

    @Operation(
            summary = "Liste tous les cours",
            description = "Retourne la liste complète des cours—accessible publiquement",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
            }
    )
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CoursDTO>>> findAll() {
        List<EntityModel<CoursDTO>> coursList = coursService.findAll()
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CoursDTO>> collection = CollectionModel.of(coursList);
        collection.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoursController.class).findAll()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(
            summary = "Détail d'un cours",
            description = "Retourne les informations d'un cours par son ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CoursDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(coursService.findById(id)));
    }

    @Operation(
            summary = "Créer un cours",
            description = "Crée un nouveau cours—PROFESSEUR ou ADMIN requis",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CoursDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemple création cours",
                                    value = """
                {
                    "nom": "Informatique",
                    "description": "Cours d'informatique générale",
                    "credits": 3,
                    "professeurId": 1
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cours créé avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<EntityModel<CoursDTO>> creer(@Valid @RequestBody CoursDTO dto) {
        return ResponseEntity.status(201).body(toModel(coursService.creer(dto)));
    }

    @Operation(
            summary = "Modifier un cours",
            description = "Met à jour les informations d'un cours",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CoursDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemple modification cours",
                                    value = """
                {
                    "nom": "Informatique Avancée",
                    "description": "Cours avancé d'informatique",
                    "credits": 4,
                    "professeurId": 1
                }
                """
                            )
                    )
            )
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CoursDTO>> modifier(
            @PathVariable Long id,
            @Valid @RequestBody CoursDTO dto) {
        return ResponseEntity.ok(toModel(coursService.modifier(id, dto)));
    }

    @Operation(
            summary = "Supprimer un cours",
            description = "Supprime un cours—ADMIN requis",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cours supprimé avec succès"),
                    @ApiResponse(responseCode = "403", description = "Accès refusé—ADMIN requis")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> supprimer(@PathVariable Long id) {
        coursService.supprimer(id);
        return ResponseEntity.ok(Map.of("message", "Cours supprimé avec succès"));
    }

    @Operation(
            summary = "Statistiques d'un cours",
            description = "Retourne les statistiques : moyenne, meilleure note, note minimale, nombre d'étudiants inscrits",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Statistiques retournées avec succès")
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}/statistiques")
    public ResponseEntity<EntityModel<StatistiquesCoursDTO>> obtenirStatistiques(@PathVariable Long id) {
        StatistiquesCoursDTO stats = coursService.obtenirStatistiques(id);
        EntityModel<StatistiquesCoursDTO> model = EntityModel.of(stats);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoursController.class).obtenirStatistiques(id)).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoursController.class).findById(id)).withRel("cours"));
        return ResponseEntity.ok(model);
    }
}
