package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.interfaces.IContratService;

import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
@Slf4j
public class ContratController {

    private final IContratService contratService;

    @PostMapping
    public ResponseEntity<Contrat> createContrat(@RequestBody Contrat contrat) {
        log.info("Creating contrat");
        Contrat created = contratService.create(contrat);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrat> getContratById(@PathVariable Long id) {
        log.info("Fetching contrat with id: {}", id);
        try {
            Contrat contrat = contratService.findById(id);
            return ResponseEntity.ok(contrat);
        } catch (ResourceNotFoundException e) {
            log.warn("Contrat not found with id: {}", id);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Contrat>> getAllContrats() {
        log.info("Fetching all contrats");
        List<Contrat> contrats = contratService.findAll();
        return ResponseEntity.ok(contrats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrat> updateContrat(@PathVariable Long id, @RequestBody Contrat contrat) {
        log.info("Updating contrat with id: {}", id);
        Contrat updated = contratService.update(id, contrat);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContrat(@PathVariable Long id) {
        log.info("Deleting contrat with id: {}", id);
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}