package tn.esprit.autoloc.service.scheduling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Scheduled;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

class SchedulerServicesTest {

    @Test
    void schedulerMethodsUseRequiredDailyCrons() throws Exception {
        Scheduled contractSchedule = ContractExpirationSchedulerService.class
                .getMethod("identifyContractsExpiringWithin48Hours")
                .getAnnotation(Scheduled.class);
        Scheduled vehicleSchedule = VehiculeSchedulerService.class
                .getMethod("terminerLocationsEtLibererVehicules")
                .getAnnotation(Scheduled.class);

        assertEquals("0 0 7 * * *", contractSchedule.cron());
        assertEquals("0 0 1 * * *", vehicleSchedule.cron());
        assertEquals("Africa/Tunis", contractSchedule.zone());
        assertEquals("Africa/Tunis", vehicleSchedule.zone());
    }

    @Test
    void expirationJobLooksAheadTwoDaysForConfirmedReservationsWithContracts() {
        IReservationRepository repository = mock(IReservationRepository.class);
        when(repository.findByContratIsNotNullAndStatutAndDateFinBetween(
                org.mockito.ArgumentMatchers.eq(StatutReservation.CONFIRMEE),
                any(LocalDate.class),
                any(LocalDate.class))).thenReturn(List.of());
        ContractExpirationSchedulerService scheduler = new ContractExpirationSchedulerService(repository);

        scheduler.identifyContractsExpiringWithin48Hours();

        LocalDate today = LocalDate.now();
        verify(repository).findByContratIsNotNullAndStatutAndDateFinBetween(
                StatutReservation.CONFIRMEE, today, today.plusDays(2));
    }

    @Test
    void vehicleJobTerminatesExpiredRentalAndMakesVehicleAvailable() {
        IReservationRepository reservations = mock(IReservationRepository.class);
        IVehiculeRepository vehicles = mock(IVehiculeRepository.class);
        Vehicule vehicule = vehicle(1L, StatutVehicule.LOUE);
        Reservation expired = reservation(1L, vehicule, LocalDate.now().minusDays(1));
        when(reservations.findByStatutAndDateFinBefore(StatutReservation.CONFIRMEE, LocalDate.now()))
                .thenReturn(List.of(expired));
        when(reservations.existsByVehicule_IdVehiculeAndStatutAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                1L, StatutReservation.CONFIRMEE, LocalDate.now(), LocalDate.now()))
                .thenReturn(false);

        new VehiculeSchedulerService(reservations, vehicles).terminerLocationsEtLibererVehicules();

        assertEquals(StatutReservation.TERMINEE, expired.getStatut());
        assertEquals(StatutVehicule.DISPONIBLE, vehicule.getStatut());
        verify(reservations).saveAll(List.of(expired));
        verify(vehicles).saveAll(List.of(vehicule));
    }

    @Test
    void vehicleRemainsRentedWhenAnotherConfirmedReservationIsActiveToday() {
        IReservationRepository reservations = mock(IReservationRepository.class);
        IVehiculeRepository vehicles = mock(IVehiculeRepository.class);
        Vehicule vehicule = vehicle(1L, StatutVehicule.LOUE);
        Reservation expired = reservation(1L, vehicule, LocalDate.now().minusDays(1));
        when(reservations.findByStatutAndDateFinBefore(StatutReservation.CONFIRMEE, LocalDate.now()))
                .thenReturn(List.of(expired));
        when(reservations.existsByVehicule_IdVehiculeAndStatutAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                1L, StatutReservation.CONFIRMEE, LocalDate.now(), LocalDate.now()))
                .thenReturn(true);

        new VehiculeSchedulerService(reservations, vehicles).terminerLocationsEtLibererVehicules();

        assertEquals(StatutReservation.TERMINEE, expired.getStatut());
        assertEquals(StatutVehicule.LOUE, vehicule.getStatut());
        verify(vehicles, never()).saveAll(any());
    }

    private Reservation reservation(Long id, Vehicule vehicule, LocalDate dateFin) {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(id);
        reservation.setDateDebut(dateFin.minusDays(2));
        reservation.setDateFin(dateFin);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setVehicule(vehicule);
        Contrat contrat = new Contrat();
        contrat.setIdContrat(id);
        reservation.setContrat(contrat);
        return reservation;
    }

    private Vehicule vehicle(Long id, StatutVehicule statut) {
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(id);
        vehicule.setStatut(statut);
        return vehicule;
    }
}
