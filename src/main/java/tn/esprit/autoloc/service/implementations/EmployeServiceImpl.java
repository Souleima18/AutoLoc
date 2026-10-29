package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.interfaces.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeDto;
import tn.esprit.autoloc.web.mapper.EmployeMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    @Override
    public EmployeDto.Response create(EmployeDto.Request request) {
        log.info("Création d'un nouvel employé");
        Employe employe = employeMapper.toEntity(request);
        employe.setAgence(getAgence(request.agenceId()));
        return employeMapper.toResponse(employeRepository.save(employe));
    }

    @Override
    public EmployeDto.Response findById(Long id) {
        log.info("Recherche de l'employé avec l'ID: {}", id);
        return employeMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<EmployeDto.Response> findAll() {
        log.info("Récupération de tous les employés");
        return employeRepository.findAll().stream().map(employeMapper::toResponse).toList();
    }

    @Override
    public EmployeDto.Response update(Long id, EmployeDto.Request request) {
        log.info("Mise à jour de l'employé avec l'ID: {}", id);
        Employe employe = getEntityById(id);
        employeMapper.updateEntity(request, employe);
        employe.setAgence(getAgence(request.agenceId()));
        return employeMapper.toResponse(employeRepository.save(employe));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de l'employé avec l'ID: {}", id);
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }

    private Employe getEntityById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    private Agence getAgence(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }
}
