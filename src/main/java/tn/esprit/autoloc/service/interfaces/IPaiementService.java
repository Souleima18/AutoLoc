package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.domain.Paiement;

public interface IPaiementService {
    Paiement findById(Long id);
    List<Paiement> findAll();
}