package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.interfaces.IReservationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;

    @Override
    public Reservation create(Reservation reservation) {
        log.info("Création d'une nouvelle réservation");
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        if (reservation.getDateDebut() != null && reservation.getDateFin() != null) {
            if (reservation.getDateFin().isBefore(reservation.getDateDebut())) {
                throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
            }
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        log.info("Recherche de la réservation avec l'ID: {}", id);
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> findAll() {
        log.info("Récupération de toutes les réservations");
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        log.info("Mise à jour de la réservation avec l'ID: {}", id);
        Reservation existant = findById(id);

        if (reservation.getDateDebut() != null) {
            existant.setDateDebut(reservation.getDateDebut());
        }
        if (reservation.getDateFin() != null) {
            if (reservation.getDateFin().isBefore(existant.getDateDebut())) {
                throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
            }
            existant.setDateFin(reservation.getDateFin());
        }
        if (reservation.getStatut() != null) {
            existant.setStatut(reservation.getStatut());
        }

        return reservationRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de la réservation avec l'ID: {}", id);
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }
}