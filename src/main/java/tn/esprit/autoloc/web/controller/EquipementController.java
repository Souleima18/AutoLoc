package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.interfaces.IEquipementService;
import tn.esprit.autoloc.web.dto.EquipementDto;

@RestController
@RequestMapping("/api/equipements")
@RequiredArgsConstructor
@Slf4j
public class EquipementController {

    private final IEquipementService equipementService;

    @PostMapping
    public ResponseEntity<EquipementDto.Response> createEquipement(
            @Valid @RequestBody EquipementDto.Request request) {
        log.info("Creating equipement with libelle: {}", request.libelle());
        return ResponseEntity.status(HttpStatus.CREATED).body(equipementService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipementDto.Response> getEquipementById(@PathVariable Long id) {
        log.info("Fetching equipement with id: {}", id);
        return ResponseEntity.ok(equipementService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<EquipementDto.Response>> getAllEquipements() {
        log.info("Fetching all equipements");
        return ResponseEntity.ok(equipementService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipementDto.Response> updateEquipement(
            @PathVariable Long id, @Valid @RequestBody EquipementDto.Request request) {
        log.info("Updating equipement with id: {}", id);
        return ResponseEntity.ok(equipementService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipement(@PathVariable Long id) {
        log.info("Deleting equipement with id: {}", id);
        equipementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
