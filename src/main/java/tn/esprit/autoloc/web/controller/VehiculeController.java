package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.interfaces.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDto;
import tn.esprit.autoloc.web.dto.affectation.VehiculeDTO;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
@Slf4j
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @PostMapping
    public ResponseEntity<VehiculeDto.Response> createVehicule(@Valid @RequestBody VehiculeDto.Request request) {
        log.info("Creating vehicule with immatriculation: {}", request.immatriculation());
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDto.Response> getVehiculeById(@PathVariable Long id) {
        log.info("Fetching vehicule with id: {}", id);
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<VehiculeDto.Response>> getAllVehicules() {
        log.info("Fetching all vehicules");
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDto.Response> updateVehicule(
            @PathVariable Long id, @Valid @RequestBody VehiculeDto.Request request) {
        log.info("Updating vehicule with id: {}", id);
        return ResponseEntity.ok(vehiculeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        log.info("Deleting vehicule with id: {}", id);
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/equipements/{idEquipement}")
    public ResponseEntity<VehiculeDTO> affecterVehiculeAEquipement(
            @PathVariable Long id,
            @PathVariable Long idEquipement) {
        return ResponseEntity.ok(vehiculeService.affecterVehiculeAEquipement(id, idEquipement));
    }

    @PostMapping("/affecter-maintenance")
    public ResponseEntity<VehiculeDTO> affecterMaintenanceAvehicule(
            @RequestParam String description,
            @RequestParam String immatriculation) {
        return ResponseEntity.ok(vehiculeService.affecterMaintenanceAvehicule(description, immatriculation));
    }
}
