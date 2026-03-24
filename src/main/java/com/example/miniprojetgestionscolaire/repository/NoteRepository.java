package com.example.miniprojetgestionscolaire.repository;



import com.example.miniprojetgestionscolaire.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByEtudiantId(Long etudiantId);

    List<Note> findByEtudiantIdAndProfesseurId(Long etudiantId, Long professeurId);


    @Query("SELECT n FROM Note n WHERE n.etudiant.classe = :classe")
    List<Note> findByClasseEtudiant(@Param("classe") String classe);

    List<Note> findByProfesseurId(Long professeurId);

    @Query("SELECT AVG(n.note) FROM Note n WHERE n.etudiant.id = :etudiantId")
    Double calculerMoyenneEtudiant(@Param("etudiantId") Long etudiantId);
}
