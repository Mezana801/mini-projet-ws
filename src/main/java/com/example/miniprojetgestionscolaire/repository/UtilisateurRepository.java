package com.example.miniprojetgestionscolaire.repository;

import com.example.miniprojetgestionscolaire.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmailAndMotDePasse(String email, String motDePasse);

    boolean existsByEmail(String email);

    Optional<Utilisateur> findByEmail(String email);
}
