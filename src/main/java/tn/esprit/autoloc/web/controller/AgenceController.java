package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.interfaces.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
@Slf4j
public class AgenceController {

    private final IAgenceService agenceService;

    @PostMapping
    public ResponseEntity<Agence> create(@RequestBody Agence data) {
        log.info("Creating agence with name: {}", data.getNom());
        Agence result = agenceService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public Agence findById(@PathVariable Long id) {
        log.info("Fetching agence with id: {}", id);
        return agenceService.findById(id);
    }

    @GetMapping
    public List<Agence> findAll() {
        log.info("Fetching all agences");
        return agenceService.findAll();
    }

    @PutMapping("/{id}")
    public Agence update(@PathVariable Long id, @RequestBody Agence data) {
        log.info("Updating agence with id: {}", id);
        return agenceService.update(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting agence with id: {}", id);
        agenceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}