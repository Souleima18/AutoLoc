package tn.esprit.autoloc.service.interfaces;

import tn.esprit.autoloc.domain.Employe;
import java.util.List;
import java.util.Optional;

public interface IEmployeService {

    // CREATE
    Employe createEmploye(Employe employe);

    // READ
    Optional<Employe> getEmployeById(Long id);
    List<Employe> getAllEmployes();

    // UPDATE
    Employe updateEmploye(Long id, Employe employe);

    // DELETE
    void deleteEmploye(Long id);
    boolean employeExists(Long id);
    long countEmployes();
}