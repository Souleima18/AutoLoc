package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.interfaces.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationDto;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final IReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDto.Response> createReservation(
            @Valid @RequestBody ReservationDto.Request request) {
        log.info("Creating reservation");
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto.Response> getReservationById(@PathVariable Long id) {
        log.info("Fetching reservation with id: {}", id);
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto.Response>> getAllReservations() {
        log.info("Fetching all reservations");
        return ResponseEntity.ok(reservationService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto.Response> updateReservation(
            @PathVariable Long id, @Valid @RequestBody ReservationDto.Request request) {
        log.info("Updating reservation with id: {}", id);
        return ResponseEntity.ok(reservationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        log.info("Deleting reservation with id: {}", id);
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
