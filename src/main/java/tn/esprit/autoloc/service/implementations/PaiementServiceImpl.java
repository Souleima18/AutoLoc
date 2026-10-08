package tn.esprit.autoloc.service.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.service.interfaces.IPaiementService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    public Paiement createPaiement(Paiement paiement) {
        log.info("Creating paiement: {} {}", paiement.getMontant(), paiement.getModePaiement());
        return paiementRepository.save(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paiement> getPaiementById(Long id) {
        log.info("Fetching paiement with id: {}", id);
        return paiementRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> getAllPaiements() {
        log.info("Fetching all paiements");
        return paiementRepository.findAll();
    }

    @Override
    public Paiement updatePaiement(Long id, Paiement paiement) {
        log.info("Updating paiement with id: {}", id);
        Paiement existingPaiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement not found with id: " + id));

        existingPaiement.setMontant(paiement.getMontant());
        existingPaiement.setDatePaiement(paiement.getDatePaiement());
        existingPaiement.setModePaiement(paiement.getModePaiement());

        return paiementRepository.save(existingPaiement);
    }

    @Override
    public void deletePaiement(Long id) {
        log.info("Deleting paiement with id: {}", id);
        paiementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean paiementExists(Long id) {
        return paiementRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPaiements() {
        return paiementRepository.count();
    }
}