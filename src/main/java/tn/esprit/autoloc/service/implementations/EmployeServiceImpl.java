package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.interfaces.IEmployeService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe create(Employe employe) {
        log.info("Création d'un nouveau employé");
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException("Un nouveau employé ne doit pas avoir d'identifiant");
        }
        if (employe.getNom() == null || employe.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire");
        }
        if (employe.getPrenom() == null || employe.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le prénom de l'employé est obligatoire");
        }
        if (employe.getRole() == null) {
            throw new IllegalArgumentException("Le rôle de l'employé est obligatoire");
        }
        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        log.info("Recherche de l'employé avec l'ID: {}", id);
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    public List<Employe> findAll() {
        log.info("Récupération de tous les employés");
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        log.info("Mise à jour de l'employé avec l'ID: {}", id);
        Employe existant = findById(id);

        if (employe.getNom() != null && !employe.getNom().isBlank()) {
            existant.setNom(employe.getNom());
        }
        if (employe.getPrenom() != null && !employe.getPrenom().isBlank()) {
            existant.setPrenom(employe.getPrenom());
        }
        if (employe.getRole() != null) {
            existant.setRole(employe.getRole());
        }

        return employeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de l'employé avec l'ID: {}", id);
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }
}