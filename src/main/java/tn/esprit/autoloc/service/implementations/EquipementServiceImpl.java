package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.service.interfaces.IEquipementService;
import tn.esprit.autoloc.web.dto.EquipementDto;
import tn.esprit.autoloc.web.mapper.EquipementMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;

    @Override
    public EquipementDto.Response create(EquipementDto.Request request) {
        log.info("Création d'un nouvel équipement");
        return equipementMapper.toResponse(equipementRepository.save(equipementMapper.toEntity(request)));
    }

    @Override
    public EquipementDto.Response findById(Long id) {
        log.info("Recherche de l'équipement avec l'ID: {}", id);
        return equipementMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<EquipementDto.Response> findAll() {
        log.info("Récupération de tous les équipements");
        return equipementRepository.findAll().stream().map(equipementMapper::toResponse).toList();
    }

    @Override
    public EquipementDto.Response update(Long id, EquipementDto.Request request) {
        log.info("Mise à jour de l'équipement avec l'ID: {}", id);
        Equipement equipement = getEntityById(id);
        equipementMapper.updateEntity(request, equipement);
        return equipementMapper.toResponse(equipementRepository.save(equipement));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de l'équipement avec l'ID: {}", id);
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipement", id);
        }
        equipementRepository.deleteById(id);
    }

    private Equipement getEntityById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
    }
}
