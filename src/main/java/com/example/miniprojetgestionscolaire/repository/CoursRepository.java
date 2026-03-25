package com.example.miniprojetgestionscolaire.repository;

import com.example.miniprojetgestionscolaire.model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoursRepository extends JpaRepository<Cours, Long> {

    List<Cours> findByProfesseurId(Long professeurId);

    List<Cours> findByNomContainingIgnoreCase(String nom);
}
