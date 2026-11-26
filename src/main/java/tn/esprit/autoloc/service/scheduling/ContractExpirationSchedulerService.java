package tn.esprit.autoloc.service.scheduling;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.repository.IReservationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractExpirationSchedulerService {

    private final IReservationRepository reservationRepository;

    @Scheduled(cron = "0 0 7 * * *", zone = "Africa/Tunis")
    public void identifyContractsExpiringWithin48Hours() {
        LocalDate today = LocalDate.now();
        LocalDate endOfWindow = today.plusDays(2);
        List<Reservation> dueReservations = reservationRepository
                .findByContratIsNotNullAndStatutAndDateFinBetween(
                        StatutReservation.CONFIRMEE, today, endOfWindow);

        if (dueReservations.isEmpty()) {
            log.info("Aucun contrat n'arrive à échéance dans les deux prochains jours.");
            return;
        }

        dueReservations.forEach(reservation -> log.warn(
                "Le contrat {} associé à la réservation {} arrive à échéance le {}.",
                reservation.getContrat().getIdContrat(),
                reservation.getIdReservation(),
                reservation.getDateFin()));
    }
}
