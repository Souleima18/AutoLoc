package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.PaiementDto;

public interface IPaiementService {
    PaiementDto.Response findById(Long id);
    List<PaiementDto.Response> findAll();
}