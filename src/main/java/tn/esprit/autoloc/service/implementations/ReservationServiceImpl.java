package tn.esprit.autoloc.service.implementations;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceConflictException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.interfaces.IReservationService;
import tn.esprit.autoloc.web.dto.ContratDto;
import tn.esprit.autoloc.web.dto.ReservationDto;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IClientRepository clientRepository;
    private final IContratRepository contratRepository;
    private final ReservationMapper reservationMapper;
    private final ContratMapper contratMapper;

    @Override
    public ReservationDto.Response create(ReservationDto.Request request) {
        log.info("Création d'une nouvelle réservation");
        validateDates(request.dateDebut(), request.dateFin());
        Reservation reservation = reservationMapper.toEntity(request);
        setRelations(reservation, request);
        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    public ReservationDto.Response findById(Long id) {
        log.info("Recherche de la réservation avec l'ID: {}", id);
        return reservationMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<ReservationDto.Response> findAll() {
        log.info("Récupération de toutes les réservations");
        return reservationRepository.findAll().stream().map(reservationMapper::toResponse).toList();
    }

    @Override
    public ReservationDto.Response update(Long id, ReservationDto.Request request) {
        log.info("Mise à jour de la réservation avec l'ID: {}", id);
        validateDates(request.dateDebut(), request.dateFin());
        Reservation reservation = getEntityById(id);
        reservationMapper.updateEntity(request, reservation);
        setRelations(reservation, request);
        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression de la réservation avec l'ID: {}", id);
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom) {
        Reservation reservation = getEntityById(idReservation);
        List<Client> clients = clientRepository.findByNomIgnoreCaseAndPrenomIgnoreCase(nom, prenom);
        if (clients.isEmpty()) {
            throw new ResourceNotFoundException("Aucun client trouvé avec le nom et prénom fournis.");
        }
        if (clients.size() > 1) {
            throw new ResourceConflictException("Plusieurs clients correspondent à ce nom et prénom.");
        }

        reservation.setClient(clients.get(0));
        return toResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO) {
        validateDates(requestDTO.dateDebut(), requestDTO.dateFin());

        Vehicule vehicule = getVehicule(requestDTO.idVehicule());
        Client client = getClient(requestDTO.idClient());

        Contrat contrat = contratMapper.toEntity(new ContratDto.Request(
                requestDTO.dateSignature(),
                requestDTO.montantTotal(),
                false));
        contrat = contratRepository.save(contrat);

        Reservation reservation = new Reservation();
        reservation.setDateDebut(requestDTO.dateDebut());
        reservation.setDateFin(requestDTO.dateFin());
        reservation.setStatut(requestDTO.statut());
        reservation.setVehicule(vehicule);
        reservation.setClient(client);
        reservation.setContrat(contrat);

        return toResponse(reservationRepository.save(reservation));
    }

    private Reservation getEntityById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    private void setRelations(Reservation reservation, ReservationDto.Request request) {
        reservation.setVehicule(getVehicule(request.vehiculeId()));
        reservation.setClient(getClient(request.clientId()));
        if (request.contratId() != null) {
            reservation.setContrat(getContrat(request.contratId()));
        }
    }

    private Vehicule getVehicule(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    private Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    private Contrat getContrat(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }

    private ReservationResponseDTO toResponse(Reservation reservation) {
        ReservationDto.Response response = reservationMapper.toResponse(reservation);
        return new ReservationResponseDTO(
                response.idReservation(),
                response.dateDebut(),
                response.dateFin(),
                response.statut(),
                response.vehiculeId(),
                response.clientId(),
                response.contratId());
    }
}
