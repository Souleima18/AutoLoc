package tn.esprit.autoloc.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.service.interfaces.IEquipementService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement createEquipement(Equipement equipement) {
        log.info("Creating equipement: {}", equipement.getLibelle());
        return equipementRepository.save(equipement);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipement> getEquipementById(Long id) {
        log.info("Fetching equipement with id: {}", id);
        return equipementRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> getAllEquipements() {
        log.info("Fetching all equipements");
        return equipementRepository.findAll();
    }

    @Override
    public Equipement updateEquipement(Long id, Equipement equipement) {
        log.info("Updating equipement with id: {}", id);
        Equipement existingEquipement = equipementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipement not found with id: " + id));

        existingEquipement.setLibelle(equipement.getLibelle());

        return equipementRepository.save(existingEquipement);
    }

    @Override
    public void deleteEquipement(Long id) {
        log.info("Deleting equipement with id: {}", id);
        equipementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean equipementExists(Long id) {
        return equipementRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countEquipements() {
        return equipementRepository.count();
    }
}