package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.interfaces.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@RequiredArgsConstructor
@Slf4j
public class EquipementController {

    private final IEquipementService equipementService;

    @PostMapping
    public ResponseEntity<Equipement> createEquipement(@RequestBody Equipement equipement) {
        log.info("Creating equipement with libelle: {}", equipement.getLibelle());
        Equipement created = equipementService.create(equipement);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipement> getEquipementById(@PathVariable Long id) {
        log.info("Fetching equipement with id: {}", id);
        try {
            Equipement equipement = equipementService.findById(id);
            return ResponseEntity.ok(equipement);
        } catch (ResourceNotFoundException e) {
            log.warn("Equipement not found with id: {}", id);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Equipement>> getAllEquipements() {
        log.info("Fetching all equipements");
        List<Equipement> equipements = equipementService.findAll();
        return ResponseEntity.ok(equipements);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipement> updateEquipement(@PathVariable Long id, @RequestBody Equipement equipement) {
        log.info("Updating equipement with id: {}", id);
        Equipement updated = equipementService.update(id, equipement);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipement(@PathVariable Long id) {
        log.info("Deleting equipement with id: {}", id);
        equipementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}