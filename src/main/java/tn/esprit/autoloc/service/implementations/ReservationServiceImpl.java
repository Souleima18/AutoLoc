package tn.esprit.autoloc.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.interfaces.IReservationService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(Reservation reservation) {
        log.info("Creating reservation from {} to {}", reservation.getDateDebut(), reservation.getDateFin());
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(Long id) {
        log.info("Fetching reservation with id: {}", id);
        return reservationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        log.info("Fetching all reservations");
        return reservationRepository.findAll();
    }

    @Override
    public Reservation updateReservation(Long id, Reservation reservation) {
        log.info("Updating reservation with id: {}", id);
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

        existingReservation.setDateDebut(reservation.getDateDebut());
        existingReservation.setDateFin(reservation.getDateFin());
        existingReservation.setStatut(reservation.getStatut());

        return reservationRepository.save(existingReservation);
    }

    @Override
    public void deleteReservation(Long id) {
        log.info("Deleting reservation with id: {}", id);
        reservationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean reservationExists(Long id) {
        return reservationRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countReservations() {
        return reservationRepository.count();
    }
}