package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.service.interfaces.IEquipementService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement create(Equipement equipement) {
        log.info("Création d'un nouvel équipement");
        if (equipement.getIdEquipement() != null) {
            throw new IllegalArgumentException("Un nouvel équipement ne doit pas avoir d'identifiant");
        }
        if (equipement.getLibelle() == null || equipement.getLibelle().isBlank()) {
            throw new IllegalArgumentException("Le libellé de l'équipement est obligatoire");
        }
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement findById(Long id) {
        log.info("Recherche de l'équipement avec l'ID: {}", id);
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
    }

    @Override
    public List<Equipement> findAll() {
        log.info("Récupération de tous les équipements");
        return equipementRepository.findAll();
    }

    @Override
    public Equipement update(Long id, Equipement equipement) {
        log.info("Mise à jour de l'équipement avec l'ID: {}", id);
        Equipement existant = findById(id);

        if (equipement.getLibelle() != null && !equipement.getLibelle().isBlank()) {
            existant.setLibelle(equipement.getLibelle());
        }

        return equipementRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de l'équipement avec l'ID: {}", id);
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipement", id);
        }
        equipementRepository.deleteById(id);
    }
}