package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.service.interfaces.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
@Slf4j
public class MaintenanceController {

    private final IMaintenanceService service;

    @PostMapping
    public ResponseEntity<Maintenance> create(@RequestBody Maintenance m) {
        log.info("Creating maintenance");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(m));
    }

    @GetMapping("/{id}")
    public Maintenance findById(@PathVariable Long id) {
        log.info("Fetching maintenance with id: {}", id);
        return service.findById(id);
    }

    @GetMapping
    public List<Maintenance> findAll() {
        log.info("Fetching all maintenances");
        return service.findAll();
    }

    @PutMapping("/{id}")
    public Maintenance update(@PathVariable Long id, @RequestBody Maintenance m) {
        log.info("Updating maintenance with id: {}", id);
        return service.update(id, m);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting maintenance with id: {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}