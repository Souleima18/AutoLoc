package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.interfaces.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
@Slf4j
public class EmployeController {

    private final IEmployeService employeService;

    @PostMapping
    public ResponseEntity<Employe> createEmploye(@RequestBody Employe employe) {
        log.info("Creating employe with name: {}", employe.getNom());
        Employe created = employeService.create(employe);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employe> getEmployeById(@PathVariable Long id) {
        log.info("Fetching employe with id: {}", id);
        try {
            Employe employe = employeService.findById(id);
            return ResponseEntity.ok(employe);
        } catch (ResourceNotFoundException e) {
            log.warn("Employe not found with id: {}", id);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Employe>> getAllEmployes() {
        log.info("Fetching all employes");
        List<Employe> employes = employeService.findAll();
        return ResponseEntity.ok(employes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employe> updateEmploye(@PathVariable Long id, @RequestBody Employe employe) {
        log.info("Updating employe with id: {}", id);
        Employe updated = employeService.update(id, employe);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        log.info("Deleting employe with id: {}", id);
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}