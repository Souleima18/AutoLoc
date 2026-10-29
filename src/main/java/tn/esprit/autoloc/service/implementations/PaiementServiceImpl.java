package tn.esprit.autoloc.service.implementations;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.service.interfaces.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementDto;
import tn.esprit.autoloc.web.mapper.PaiementMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;
    private final PaiementMapper paiementMapper;

    @Override
    public PaiementDto.Response findById(Long id) {
        log.info("Recherche du paiement avec l'ID: {}", id);
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
        return paiementMapper.toResponse(paiement);
    }

    @Override
    public List<PaiementDto.Response> findAll() {
        log.info("Récupération de tous les paiements");
        return paiementRepository.findAll().stream().map(paiementMapper::toResponse).toList();
    }
}
