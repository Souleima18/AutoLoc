package tn.esprit.autoloc.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.interfaces.IEmployeService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe createEmploye(Employe employe) {
        log.info("Creating employe: {} {}", employe.getNom(), employe.getPrenom());
        return employeRepository.save(employe);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employe> getEmployeById(Long id) {
        log.info("Fetching employe with id: {}", id);
        return employeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> getAllEmployes() {
        log.info("Fetching all employes");
        return employeRepository.findAll();
    }

    @Override
    public Employe updateEmploye(Long id, Employe employe) {
        log.info("Updating employe with id: {}", id);
        Employe existingEmploye = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employe not found with id: " + id));

        existingEmploye.setNom(employe.getNom());
        existingEmploye.setPrenom(employe.getPrenom());
        existingEmploye.setRole(employe.getRole());

        return employeRepository.save(existingEmploye);
    }

    @Override
    public void deleteEmploye(Long id) {
        log.info("Deleting employe with id: {}", id);
        employeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean employeExists(Long id) {
        return employeRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countEmployes() {
        return employeRepository.count();
    }
}