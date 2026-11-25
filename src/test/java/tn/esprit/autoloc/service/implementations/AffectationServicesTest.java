package tn.esprit.autoloc.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;
import tn.esprit.autoloc.web.mapper.ReservationMapper;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

class AffectationServicesTest {

    @Test
    void affectReservationToClientUsesUniqueNameMatch() {
        IReservationRepository reservations = mock(IReservationRepository.class);
        IClientRepository clients = mock(IClientRepository.class);
        Client client = client(2L);
        Reservation reservation = reservation(1L, vehicle(3L), client(1L), contract(4L));
        when(reservations.findById(1L)).thenReturn(Optional.of(reservation));
        when(clients.findByNomIgnoreCaseAndPrenomIgnoreCase("Martin", "Amira")).thenReturn(List.of(client));
        when(reservations.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReservationServiceImpl service = reservationService(
                reservations, mock(IVehiculeRepository.class), clients, mock(IContratRepository.class));

        var response = service.affecterReservationAClient(1L, "Martin", "Amira");

        assertEquals(2L, response.clientId());
        verify(reservations).save(reservation);
    }

    @Test
    void affectVehicleToEquipmentAddsAssociation() {
        IVehiculeRepository vehicles = mock(IVehiculeRepository.class);
        IEquipementRepository equipmentRepository = mock(IEquipementRepository.class);
        Vehicule vehicle = vehicle(3L);
        vehicle.setEquipements(new HashSet<>());
        Equipement equipment = new Equipement();
        equipment.setIdEquipement(5L);
        when(vehicles.findById(3L)).thenReturn(Optional.of(vehicle));
        when(equipmentRepository.findById(5L)).thenReturn(Optional.of(equipment));
        when(vehicles.save(any(Vehicule.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehiculeServiceImpl service = vehicleService(vehicles, equipmentRepository, mock(IMaintenanceRepository.class));

        var response = service.affecterVehiculeAEquipement(3L, 5L);

        assertEquals(1, vehicle.getEquipements().size());
        assertEquals(3L, response.idVehicule());
        verify(vehicles).save(vehicle);
    }

    @Test
    void affectMaintenanceCreatesRecordAndMarksVehicleUnavailable() {
        IVehiculeRepository vehicles = mock(IVehiculeRepository.class);
        IMaintenanceRepository maintenances = mock(IMaintenanceRepository.class);
        Vehicule vehicle = vehicle(3L);
        when(vehicles.findByImmatriculationIgnoreCase("TN-123-XYZ")).thenReturn(Optional.of(vehicle));
        when(vehicles.save(any(Vehicule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(maintenances.save(any(Maintenance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehiculeServiceImpl service = vehicleService(vehicles, mock(IEquipementRepository.class), maintenances);

        var response = service.affecterMaintenanceAvehicule("Vidange", "TN-123-XYZ");

        assertEquals(StatutVehicule.MAINTENANCE, response.statut());
        verify(maintenances).save(any(Maintenance.class));
    }

    @Test
    void affectContractToReservationCalculatesAmountFromVehicleRate() {
        IReservationRepository reservations = mock(IReservationRepository.class);
        IContratRepository contracts = mock(IContratRepository.class);
        LocalDate start = LocalDate.of(2026, 12, 1);
        LocalDate end = LocalDate.of(2026, 12, 4);
        Reservation reservation = reservation(1L, vehicle(3L), client(2L), null);
        reservation.setDateDebut(start);
        reservation.setDateFin(end);
        when(reservations.findByDateDebutAndDateFin(start, end)).thenReturn(List.of(reservation));
        when(contracts.save(any(Contrat.class))).thenAnswer(invocation -> {
            Contrat contrat = invocation.getArgument(0);
            contrat.setIdContrat(9L);
            return contrat;
        });
        when(reservations.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ContratServiceImpl service = new ContratServiceImpl(
                contracts, reservations, Mappers.getMapper(ContratMapper.class));

        var response = service.affecterContratAReservation(LocalDate.of(2026, 11, 30), start, end);

        assertEquals(BigDecimal.valueOf(300), response.montantTotal());
        assertEquals(9L, reservation.getContrat().getIdContrat());
    }

    @Test
    void createReservationAndContractSavesBothWithRelations() {
        IReservationRepository reservations = mock(IReservationRepository.class);
        IContratRepository contracts = mock(IContratRepository.class);
        IVehiculeRepository vehicles = mock(IVehiculeRepository.class);
        IClientRepository clients = mock(IClientRepository.class);
        LocalDate start = LocalDate.of(2026, 12, 1);
        LocalDate end = LocalDate.of(2026, 12, 2);
        when(vehicles.findById(3L)).thenReturn(Optional.of(vehicle(3L)));
        when(clients.findById(2L)).thenReturn(Optional.of(client(2L)));
        when(contracts.save(any(Contrat.class))).thenAnswer(invocation -> {
            Contrat contrat = invocation.getArgument(0);
            contrat.setIdContrat(9L);
            return contrat;
        });
        when(reservations.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReservationServiceImpl service = reservationService(
                reservations, vehicles, clients, contracts);
        ReservationWithContratRequestDTO request = new ReservationWithContratRequestDTO(
                LocalDate.of(2026, 11, 30),
                start,
                end,
                3L,
                2L,
                BigDecimal.valueOf(100),
                StatutReservation.EN_ATTENTE);

        var response = service.creerReservationEtContratAssocie(request);

        assertEquals(3L, response.vehiculeId());
        assertEquals(2L, response.clientId());
        assertEquals(9L, response.contratId());
        verify(contracts).save(any(Contrat.class));
        verify(reservations).save(any(Reservation.class));
    }

    private ReservationServiceImpl reservationService(
            IReservationRepository reservations,
            IVehiculeRepository vehicles,
            IClientRepository clients,
            IContratRepository contracts) {
        return new ReservationServiceImpl(
                reservations,
                vehicles,
                clients,
                contracts,
                Mappers.getMapper(ReservationMapper.class),
                Mappers.getMapper(ContratMapper.class));
    }

    private VehiculeServiceImpl vehicleService(
            IVehiculeRepository vehicles,
            IEquipementRepository equipment,
            IMaintenanceRepository maintenances) {
        return new VehiculeServiceImpl(
                vehicles,
                mock(IAgenceRepository.class),
                equipment,
                maintenances,
                Mappers.getMapper(VehiculeMapper.class));
    }

    private Client client(Long id) {
        Client client = new Client();
        client.setIdClient(id);
        client.setNom("Martin");
        client.setPrenom("Amira");
        return client;
    }

    private Contrat contract(Long id) {
        Contrat contrat = new Contrat();
        contrat.setIdContrat(id);
        contrat.setDateSignature(LocalDate.of(2026, 11, 1));
        contrat.setMontantTotal(BigDecimal.valueOf(100));
        contrat.setValide(false);
        return contrat;
    }

    private Reservation reservation(Long id, Vehicule vehicle, Client client, Contrat contrat) {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(id);
        reservation.setDateDebut(LocalDate.of(2026, 12, 1));
        reservation.setDateFin(LocalDate.of(2026, 12, 2));
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        reservation.setVehicule(vehicle);
        reservation.setClient(client);
        reservation.setContrat(contrat);
        return reservation;
    }

    private Vehicule vehicle(Long id) {
        Agence agency = new Agence();
        agency.setIdAgence(1L);
        Vehicule vehicle = new Vehicule();
        vehicle.setIdVehicule(id);
        vehicle.setImmatriculation("TN-123-XYZ");
        vehicle.setMarque("Toyota");
        vehicle.setModele("Yaris");
        vehicle.setCategorie(CategorieVehicule.CITADINE);
        vehicle.setTarifJournalier(BigDecimal.valueOf(100));
        vehicle.setStatut(StatutVehicule.DISPONIBLE);
        vehicle.setAgence(agency);
        return vehicle;
    }
}
