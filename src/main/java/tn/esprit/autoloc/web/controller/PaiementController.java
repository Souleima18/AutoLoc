package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.interfaces.IPaiementService;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@Slf4j
public class PaiementController {

    private final IPaiementService service;

    @GetMapping("/{id}")
    public Paiement findById(@PathVariable Long id) {
        log.info("Fetching paiement with id: {}", id);
        return service.findById(id);
    }

    @GetMapping
    public List<Paiement> findAll() {
        log.info("Fetching all paiements");
        return service.findAll();
    }
}