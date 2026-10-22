package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.service.interfaces.IPaiementService;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    public Paiement findById(Long id) {
        log.info("Recherche du paiement avec l'ID: {}", id);
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }

    @Override
    public List<Paiement> findAll() {
        log.info("Récupération de tous les paiements");
        return paiementRepository.findAll();
    }
}