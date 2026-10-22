package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.interfaces.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
@Slf4j
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @PostMapping
    public ResponseEntity<Vehicule> createVehicule(@RequestBody Vehicule vehicule) {
        log.info("Creating vehicule with immatriculation: {}", vehicule.getImmatriculation());
        Vehicule created = vehiculeService.create(vehicule);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicule> getVehiculeById(@PathVariable Long id) {
        log.info("Fetching vehicule with id: {}", id);
        try {
            Vehicule vehicule = vehiculeService.findById(id);
            return ResponseEntity.ok(vehicule);
        } catch (ResourceNotFoundException e) {
            log.warn("Vehicule not found with id: {}", id);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Vehicule>> getAllVehicules() {
        log.info("Fetching all vehicules");
        List<Vehicule> vehicules = vehiculeService.findAll();
        return ResponseEntity.ok(vehicules);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicule> updateVehicule(@PathVariable Long id, @RequestBody Vehicule vehicule) {
        log.info("Updating vehicule with id: {}", id);
        Vehicule updated = vehiculeService.update(id, vehicule);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        log.info("Deleting vehicule with id: {}", id);
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}