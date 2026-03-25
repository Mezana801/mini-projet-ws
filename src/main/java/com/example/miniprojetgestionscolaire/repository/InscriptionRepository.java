package com.example.miniprojetgestionscolaire.repository;

import com.example.miniprojetgestionscolaire.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    List<Inscription> findByEtudiantId(Long etudiantId);

    List<Inscription> findByCoursId(Long coursId);

    Optional<Inscription> findByEtudiantIdAndCoursId(Long etudiantId, Long coursId);

    boolean existsByEtudiantIdAndCoursId(Long etudiantId, Long coursId);
}
