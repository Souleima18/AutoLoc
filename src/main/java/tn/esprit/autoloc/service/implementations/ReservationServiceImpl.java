package tn.esprit.autoloc.service.implementations;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.interfaces.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationDto;
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

    private Reservation getEntityById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    private void setRelations(Reservation reservation, ReservationDto.Request request) {
        reservation.setVehicule(getVehicule(request.vehiculeId()));
        reservation.setClient(getClient(request.clientId()));
        reservation.setContrat(getContrat(request.contratId()));
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
}
