package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.interfaces.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeDto;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
@Slf4j
public class EmployeController {

    private final IEmployeService employeService;

    @PostMapping
    public ResponseEntity<EmployeDto.Response> createEmploye(@Valid @RequestBody EmployeDto.Request request) {
        log.info("Creating employe with name: {}", request.nom());
        return ResponseEntity.status(HttpStatus.CREATED).body(employeService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeDto.Response> getEmployeById(@PathVariable Long id) {
        log.info("Fetching employe with id: {}", id);
        return ResponseEntity.ok(employeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeDto.Response>> getAllEmployes() {
        log.info("Fetching all employes");
        return ResponseEntity.ok(employeService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeDto.Response> updateEmploye(
            @PathVariable Long id, @Valid @RequestBody EmployeDto.Request request) {
        log.info("Updating employe with id: {}", id);
        return ResponseEntity.ok(employeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Long id) {
        log.info("Deleting employe with id: {}", id);
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
