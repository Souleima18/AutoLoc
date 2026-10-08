package tn.esprit.autoloc.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.interfaces.IContratService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    // ============ CREATE ============
    @Override
    public Contrat createContrat(Contrat contrat) {
        log.info("Creating contrat with montant: {}", contrat.getMontantTotal());

        if (contrat.getMontantTotal().signum() <= 0) {
            throw new RuntimeException("Montant total must be positive");
        }

        contrat.setValide(false); // Par défaut, un nouveau contrat n'est pas validé
        return contratRepository.save(contrat);
    }

    // ============ READ ============
    @Override
    @Transactional(readOnly = true)
    public Optional<Contrat> getContratById(Long id) {
        log.info("Fetching contrat with id: {}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid contrat id: {}", id);
            return Optional.empty();
        }

        return contratRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> getAllContrats() {
        log.info("Fetching all contrats");
        List<Contrat> contrats = contratRepository.findAll();
        log.info("Found {} contrats", contrats.size());
        return contrats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> getContratsByValide(Boolean valide) {
        log.info("Fetching contrats with valide: {}", valide);
        return contratRepository.findAll().stream()
                .filter(c -> c.getValide().equals(valide))
                .collect(Collectors.toList());
    }

    // ============ UPDATE ============
    @Override
    public Contrat updateContrat(Long id, Contrat contrat) {
        log.info("Updating contrat with id: {}", id);

        Contrat existingContrat = contratRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrat not found with id: " + id));

        if (contrat.getMontantTotal().signum() <= 0) {
            throw new RuntimeException("Montant total must be positive");
        }

        existingContrat.setDateSignature(contrat.getDateSignature());
        existingContrat.setMontantTotal(contrat.getMontantTotal());
        existingContrat.setValide(contrat.getValide());

        log.info("Contrat updated successfully");
        return contratRepository.save(existingContrat);
    }

    @Override
    public Contrat validateContrat(Long id) {
        log.info("Validating contrat with id: {}", id);

        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrat not found with id: " + id));

        if (contrat.getValide()) {
            log.warn("Contrat with id {} is already validated", id);
            throw new RuntimeException("Contrat is already validated");
        }

        contrat.setValide(true);
        log.info("Contrat validated successfully");
        return contratRepository.save(contrat);
    }

    // ============ DELETE ============
    @Override
    public void deleteContrat(Long id) {
        log.info("Deleting contrat with id: {}", id);

        if (!contratExists(id)) {
            log.warn("Contrat not found with id: {}", id);
            throw new RuntimeException("Contrat not found with id: " + id);
        }

        Contrat contrat = contratRepository.findById(id).get();

        // Vérifier s'il y a des paiements associés
        if (contrat.getPaiements() != null && !contrat.getPaiements().isEmpty()) {
            log.info("Deleting {} associated paiements", contrat.getPaiements().size());
        }

        contratRepository.deleteById(id);
        log.info("Contrat deleted successfully with all associated paiements");
    }

    @Override
    public void deleteAllContrats() {
        log.warn("Deleting ALL contrats");
        contratRepository.deleteAll();
        log.info("All contrats deleted");
    }

    // ============ UTILITY ============
    @Override
    @Transactional(readOnly = true)
    public boolean contratExists(Long id) {
        return contratRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countContrats() {
        long count = contratRepository.count();
        log.info("Total contrats in database: {}", count);
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public long countContratsByValide(Boolean valide) {
        long count = contratRepository.findAll().stream()
                .filter(c -> c.getValide().equals(valide))
                .count();
        log.info("Total contrats with valide={}: {}", valide, count);
        return count;
    }
}