package tn.esprit.autoloc.web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.interfaces.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementDto;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@Slf4j
public class PaiementController {

    private final IPaiementService service;

    @GetMapping("/{id}")
    public ResponseEntity<PaiementDto.Response> findById(@PathVariable Long id) {
        log.info("Fetching paiement with id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaiementDto.Response>> findAll() {
        log.info("Fetching all paiements");
        return ResponseEntity.ok(service.findAll());
    }
}
