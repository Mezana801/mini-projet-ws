package com.example.miniprojetgestionscolaire.controller;


import com.example.miniprojetgestionscolaire.dto.NoteDTO;
import com.example.miniprojetgestionscolaire.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/notes")
@Tag(name = "Notes", description = "Gestion des notes (ADMIN,PROFESSEUR uniquement)")
@SecurityRequirement(name = "Bearer Authentication")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Operation(
            summary = "Ajouter une note",
            description = "Ajoute une note à un étudiant de la classe du professeur connecté",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoteDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemple ajout note",
                                    value = """
                    {
                        "etudiantId": 1,
                        "note": 15.5,
                        "commentaire": "Bon travail"
                    }
                    """
                            )
                    )
            )
    )
    @PostMapping
    public ResponseEntity<NoteDTO> ajouter(
            @Valid @RequestBody NoteDTO dto) {
        return ResponseEntity.status(201).body(noteService.ajouter(dto));
    }

    @Operation(
            summary = "Modifier une note",
            description = "Modifie une note existante, uniquement si elle vous appartient",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoteDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemple modification note",
                                    value = """
                    {
                        "etudiantId": 1,
                        "note": 17.0,
                        "commentaire": "Excellent travail !"
                    }
                    """
                            )
                    )
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> modifier(
            @PathVariable Long id,
            @Valid @RequestBody NoteDTO dto) {
        return ResponseEntity.ok(noteService.modifier(id, dto));
    }

    @Operation(
            summary = "Notes d'un étudiant",
            description = "Retourne les notes d'un étudiant, filtrées par classe du professeur connecté"
    )
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<NoteDTO>> obtenirNotesEtudiant(
            @PathVariable Long etudiantId) {
        return ResponseEntity.ok(noteService.obtenirNotesEtudiant(etudiantId));
    }

    @Operation(
            summary = "Toutes les notes",
            description = """
        Retourne toutes les notes selon le rôle connecté :
        - ADMIN : Toutes les notes de tous les étudiants
        - PROFESSEUR : Uniquement les notes de sa classe
        - ETUDIANT : Accès refusé
    """
    )
    @GetMapping
    public ResponseEntity<List<NoteDTO>> obtenirToutesLesNotes() {
        return ResponseEntity.ok(noteService.obtenirToutesLesNotes());
    }
}
