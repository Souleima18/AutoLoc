package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.interfaces.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDto;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
@Slf4j
public class AgenceController {

    private final IAgenceService agenceService;

    @PostMapping
    public ResponseEntity<AgenceDto.Response> create(@Valid @RequestBody AgenceDto.Request request) {
        log.info("Creating agence with name: {}", request.nom());
        return ResponseEntity.status(HttpStatus.CREATED).body(agenceService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenceDto.Response> findById(@PathVariable Long id) {
        log.info("Fetching agence with id: {}", id);
        return ResponseEntity.ok(agenceService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AgenceDto.Response>> findAll() {
        log.info("Fetching all agences");
        return ResponseEntity.ok(agenceService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgenceDto.Response> update(
            @PathVariable Long id, @Valid @RequestBody AgenceDto.Request request) {
        log.info("Updating agence with id: {}", id);
        return ResponseEntity.ok(agenceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting agence with id: {}", id);
        agenceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
