package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.interfaces.IClientService;
import tn.esprit.autoloc.web.dto.ClientDto;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final IClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDto.Response> createClient(@Valid @RequestBody ClientDto.Request request) {
        log.info("Creating client with email: {}", request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto.Response> getClientById(@PathVariable Long id) {
        log.info("Fetching client with id: {}", id);
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClientDto.Response>> getAllClients() {
        log.info("Fetching all clients");
        return ResponseEntity.ok(clientService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto.Response> updateClient(
            @PathVariable Long id, @Valid @RequestBody ClientDto.Request request) {
        log.info("Updating client with id: {}", id);
        return ResponseEntity.ok(clientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("Deleting client with id: {}", id);
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
