package com.example.miniprojetgestionscolaire.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GestionnaireErreurs {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> gererErreur(RuntimeException e) {
        return ResponseEntity.status(400).body(Map.of("erreur", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> gererErreurServeur(Exception e) {
        return ResponseEntity.status(500).body(Map.of("erreur", "Erreur interne du serveur"));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> gererErreurValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> erreurs = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(erreur -> {
            String champ = ((FieldError) erreur).getField();
            String message = erreur.getDefaultMessage();
            erreurs.put(champ, message);
        });

        return ResponseEntity.status(400).body(erreurs);
    }
}
