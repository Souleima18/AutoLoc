package tn.esprit.autoloc.service.scheduling;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculeSchedulerService {

    private final IReservationRepository reservationRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Scheduled(cron = "0 0 1 * * *", zone = "Africa/Tunis")
    @Transactional
    public void terminerLocationsEtLibererVehicules() {
        LocalDate today = LocalDate.now();
        List<Reservation> expiredReservations = reservationRepository
                .findByStatutAndDateFinBefore(StatutReservation.CONFIRMEE, today);

        if (expiredReservations.isEmpty()) {
            log.info("Aucune location à clôturer aujourd'hui.");
            return;
        }

        expiredReservations.forEach(
                reservation -> reservation.setStatut(StatutReservation.TERMINEE));
        reservationRepository.saveAll(expiredReservations);

        Map<Long, Vehicule> affectedVehicles = expiredReservations.stream()
                .map(Reservation::getVehicule)
                .filter(Objects::nonNull)
                .filter(vehicule -> vehicule.getIdVehicule() != null)
                .collect(Collectors.toMap(
                        Vehicule::getIdVehicule,
                        Function.identity(),
                        (first, ignored) -> first));

        List<Vehicule> availableVehicles = affectedVehicles.values().stream()
                .filter(vehicule -> vehicule.getStatut() == StatutVehicule.LOUE)
                .filter(vehicule -> !reservationRepository
                        .existsByVehicule_IdVehiculeAndStatutAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                                vehicule.getIdVehicule(),
                                StatutReservation.CONFIRMEE,
                                today,
                                today))
                .peek(vehicule -> vehicule.setStatut(StatutVehicule.DISPONIBLE))
                .toList();

        if (!availableVehicles.isEmpty()) {
            vehiculeRepository.saveAll(availableVehicles);
        }
        log.info(
                "{} location(s) clôturée(s), {} véhicule(s) remis à disposition.",
                expiredReservations.size(),
                availableVehicles.size());
    }
}
