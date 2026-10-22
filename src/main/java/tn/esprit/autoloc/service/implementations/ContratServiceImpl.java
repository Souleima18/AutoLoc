package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.interfaces.IContratService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat create(Contrat contrat) {
        log.info("Création d'un nouveau contrat");
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        if (contrat.getMontantTotal() != null && contrat.getMontantTotal().doubleValue() < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat findById(Long id) {
        log.info("Recherche du contrat avec l'ID: {}", id);
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public List<Contrat> findAll() {
        log.info("Récupération de tous les contrats");
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        log.info("Mise à jour du contrat avec l'ID: {}", id);
        Contrat existant = findById(id);

        if (contrat.getMontantTotal() != null) {
            if (contrat.getMontantTotal().doubleValue() < 0) {
                throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
            }
            existant.setMontantTotal(contrat.getMontantTotal());
        }
        if (contrat.getValide() != null) {
            existant.setValide(contrat.getValide());
        }

        return contratRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Suppression du contrat avec l'ID: {}", id);
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }
}