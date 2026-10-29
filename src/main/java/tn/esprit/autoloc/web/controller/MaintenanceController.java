package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.interfaces.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceDto;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
@Slf4j
public class MaintenanceController {

    private final IMaintenanceService service;

    @PostMapping
    public ResponseEntity<MaintenanceDto.Response> create(@Valid @RequestBody MaintenanceDto.Request request) {
        log.info("Creating maintenance");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceDto.Response> findById(@PathVariable Long id) {
        log.info("Fetching maintenance with id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceDto.Response>> findAll() {
        log.info("Fetching all maintenances");
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDto.Response> update(
            @PathVariable Long id, @Valid @RequestBody MaintenanceDto.Request request) {
        log.info("Updating maintenance with id: {}", id);
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting maintenance with id: {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
