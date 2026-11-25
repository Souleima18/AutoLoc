package tn.esprit.autoloc.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceConflictException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.interfaces.IContratService;
import tn.esprit.autoloc.web.dto.ContratDto;
import tn.esprit.autoloc.web.mapper.ContratMapper;
import tn.esprit.autoloc.web.dto.affectation.ContratDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @Override
    public ContratDto.Response create(ContratDto.Request request) {
        log.info("Création d'un nouveau contrat");
        return contratMapper.toResponse(contratRepository.save(contratMapper.toEntity(request)));
    }

    @Override
    public ContratDto.Response findById(Long id) {
        log.info("Recherche du contrat avec l'ID: {}", id);
        return contratMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<ContratDto.Response> findAll() {
        log.info("Récupération de tous les contrats");
        return contratRepository.findAll().stream().map(contratMapper::toResponse).toList();
    }

    @Override
    public ContratDto.Response update(Long id, ContratDto.Request request) {
        log.info("Mise à jour du contrat avec l'ID: {}", id);
        Contrat contrat = getEntityById(id);
        contratMapper.updateEntity(request, contrat);
        return contratMapper.toResponse(contratRepository.save(contrat));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du contrat avec l'ID: {}", id);
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ContratDTO affecterContratAReservation(
            LocalDate dateSignature,
            LocalDate dateDebut,
            LocalDate dateFin) {
        if (dateSignature == null || dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates de signature et de réservation sont obligatoires.");
        }
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }

        List<Reservation> reservations = reservationRepository.findByDateDebutAndDateFin(dateDebut, dateFin);
        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Aucune réservation trouvée pour la période " + dateDebut + " au " + dateFin);
        }
        if (reservations.size() > 1) {
            throw new ResourceConflictException(
                    "Plusieurs réservations correspondent à cette période; l'affectation est ambiguë.");
        }

        Reservation reservation = reservations.get(0);
        if (reservation.getContrat() != null) {
            throw new ResourceConflictException("Cette réservation possède déjà un contrat.");
        }

        Contrat contrat = new Contrat();
        contrat.setDateSignature(dateSignature);
        long duree = Math.max(1, ChronoUnit.DAYS.between(dateDebut, dateFin));
        contrat.setMontantTotal(reservation.getVehicule().getTarifJournalier()
                .multiply(BigDecimal.valueOf(duree)));
        contrat.setValide(false);
        contratRepository.save(contrat);
        reservation.setContrat(contrat);
        reservationRepository.save(reservation);
        return toContratDTO(contrat);
    }

    private Contrat getEntityById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    private ContratDTO toContratDTO(Contrat contrat) {
        ContratDto.Response response = contratMapper.toResponse(contrat);
        return new ContratDTO(
                response.idContrat(),
                response.dateSignature(),
                response.montantTotal(),
                response.valide());
    }
}
