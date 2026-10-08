package tn.esprit.autoloc.service.interfaces;

import tn.esprit.autoloc.domain.Reservation;
import java.util.List;
import java.util.Optional;

public interface IReservationService {

    // CREATE
    Reservation createReservation(Reservation reservation);

    // READ
    Optional<Reservation> getReservationById(Long id);
    List<Reservation> getAllReservations();

    // UPDATE
    Reservation updateReservation(Long id, Reservation reservation);

    // DELETE
    void deleteReservation(Long id);
    boolean reservationExists(Long id);
    long countReservations();
}