package com.example.miniprojetgestionscolaire.controller;

import com.example.miniprojetgestionscolaire.dto.InscriptionDTO;
import com.example.miniprojetgestionscolaire.service.InscriptionService;
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
@RequestMapping("/api/inscriptions")
@Tag(name = "Inscriptions", description = "Gestion des inscriptions aux cours")
@SecurityRequirement(name = "Bearer Authentication")
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    private EntityModel<InscriptionDTO> toModel(InscriptionDTO dto) {
        EntityModel<InscriptionDTO> model = EntityModel.of(dto);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(InscriptionController.class).obtenirInscriptionsEtudiant(dto.getEtudiantId())).withRel("inscriptions-etudiant"));
        if (dto.getEtudiantId() != null) {
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EtudiantController.class).findById(dto.getEtudiantId())).withRel("etudiant"));
        }
        if (dto.getCoursId() != null) {
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoursController.class).findById(dto.getCoursId())).withRel("cours"));
        }
        return model;
    }

    @Operation(
            summary = "Inscrire un étudiant à un cours",
            description = "Crée une inscription entre un étudiant et un cours",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InscriptionDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemple inscription",
                                    value = """
                {
                    "etudiantId": 1,
                    "coursId": 1
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Inscription créée avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides ou étudiant déjà inscrit")
            }
    )
    @PostMapping
    public ResponseEntity<EntityModel<InscriptionDTO>> inscrire(@Valid @RequestBody InscriptionDTO dto) {
        return ResponseEntity.status(201).body(toModel(inscriptionService.inscrire(dto)));
    }

    @Operation(
            summary = "Désinscrire un étudiant",
            description = "Supprime une inscription par son ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Désinscription effectuée avec succès"),
                    @ApiResponse(responseCode = "404", description = "Inscription introuvable")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> desinscrire(@PathVariable Long id) {
        inscriptionService.desinscrire(id);
        return ResponseEntity.ok(Map.of("message", "Désinscription effectuée avec succès"));
    }

    @Operation(
            summary = "Cours d'un étudiant",
            description = "Retourne la liste des inscriptions (cours) d'un étudiant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
            }
    )
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<CollectionModel<EntityModel<InscriptionDTO>>> obtenirInscriptionsEtudiant(
            @PathVariable Long etudiantId) {
        List<EntityModel<InscriptionDTO>> inscriptions = inscriptionService.obtenirInscriptionsEtudiant(etudiantId)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<InscriptionDTO>> collection = CollectionModel.of(inscriptions);
        collection.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(InscriptionController.class).obtenirInscriptionsEtudiant(etudiantId)).withSelfRel());
        collection.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EtudiantController.class).findById(etudiantId)).withRel("etudiant"));
        return ResponseEntity.ok(collection);
    }
}
