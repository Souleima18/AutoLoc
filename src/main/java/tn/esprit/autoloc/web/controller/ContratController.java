package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.interfaces.IContratService;
import tn.esprit.autoloc.web.dto.ContratDto;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
@Slf4j
public class ContratController {

    private final IContratService contratService;

    @PostMapping
    public ResponseEntity<ContratDto.Response> createContrat(@Valid @RequestBody ContratDto.Request request) {
        log.info("Creating contrat");
        return ResponseEntity.status(HttpStatus.CREATED).body(contratService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratDto.Response> getContratById(@PathVariable Long id) {
        log.info("Fetching contrat with id: {}", id);
        return ResponseEntity.ok(contratService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ContratDto.Response>> getAllContrats() {
        log.info("Fetching all contrats");
        return ResponseEntity.ok(contratService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratDto.Response> updateContrat(
            @PathVariable Long id, @Valid @RequestBody ContratDto.Request request) {
        log.info("Updating contrat with id: {}", id);
        return ResponseEntity.ok(contratService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContrat(@PathVariable Long id) {
        log.info("Deleting contrat with id: {}", id);
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
