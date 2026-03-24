package com.example.miniprojetgestionscolaire.repository;




import com.example.miniprojetgestionscolaire.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {


    Optional<Professeur> findByUtilisateurId(Long utilisateurId);

    List<Professeur> findByClasseEnseignee(String classeEnseignee);
}
