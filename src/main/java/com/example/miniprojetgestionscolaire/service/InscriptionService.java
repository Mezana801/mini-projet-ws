package com.example.miniprojetgestionscolaire.service;

import com.example.miniprojetgestionscolaire.dto.InscriptionDTO;
import com.example.miniprojetgestionscolaire.model.Cours;
import com.example.miniprojetgestionscolaire.model.Etudiant;
import com.example.miniprojetgestionscolaire.model.Inscription;
import com.example.miniprojetgestionscolaire.repository.CoursRepository;
import com.example.miniprojetgestionscolaire.repository.EtudiantRepository;
import com.example.miniprojetgestionscolaire.repository.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscriptionService {

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private CoursRepository coursRepository;

    private InscriptionDTO inscriptionToDto(Inscription inscription) {
        InscriptionDTO dto = new InscriptionDTO();
        dto.setId(inscription.getId());
        dto.setEtudiantId(inscription.getEtudiant().getId());
        dto.setNomEtudiant(inscription.getEtudiant().getUtilisateur().getNom());
        dto.setCoursId(inscription.getCours().getId());
        dto.setNomCours(inscription.getCours().getNom());
        dto.setDateInscription(inscription.getDateInscription().toString());
        return dto;
    }

    public InscriptionDTO inscrire(InscriptionDTO dto) {
        Etudiant etudiant = etudiantRepository.findById(dto.getEtudiantId())
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable avec l'id : " + dto.getEtudiantId()));

        Cours cours = coursRepository.findById(dto.getCoursId())
                .orElseThrow(() -> new RuntimeException("Cours introuvable avec l'id : " + dto.getCoursId()));

        if (inscriptionRepository.existsByEtudiantIdAndCoursId(dto.getEtudiantId(), dto.getCoursId())) {
            throw new RuntimeException("L'étudiant est déjà inscrit à ce cours");
        }

        Inscription inscription = new Inscription(etudiant, cours);

        return inscriptionToDto(inscriptionRepository.save(inscription));
    }

    public void desinscrire(Long id) {
        if (!inscriptionRepository.existsById(id)) {
            throw new RuntimeException("Inscription introuvable avec l'id : " + id);
        }
        inscriptionRepository.deleteById(id);
    }

    public List<InscriptionDTO> obtenirInscriptionsEtudiant(Long etudiantId) {
        if (!etudiantRepository.existsById(etudiantId)) {
            throw new RuntimeException("Etudiant introuvable avec l'id : " + etudiantId);
        }

        return inscriptionRepository.findByEtudiantId(etudiantId)
                .stream()
                .map(this::inscriptionToDto)
                .collect(Collectors.toList());
    }
}
