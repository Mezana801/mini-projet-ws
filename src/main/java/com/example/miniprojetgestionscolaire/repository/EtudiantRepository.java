package com.example.miniprojetgestionscolaire.repository;

import com.example.miniprojetgestionscolaire.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Optional<Etudiant> findByUtilisateurId(Long utilisateurId);

    List<Etudiant> findByClasse(String classe);

    List<Etudiant> findByFiliere(String filiere);

    List<Etudiant> findByClasseAndFiliere(String classe, String filiere);

    boolean existsByNumeroEtudiant(String numeroEtudiant);
}
