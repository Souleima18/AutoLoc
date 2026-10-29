package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.ContratDto;

public interface IContratService {
    ContratDto.Response create(ContratDto.Request contrat);
    ContratDto.Response findById(Long id);
    List<ContratDto.Response> findAll();
    ContratDto.Response update(Long id, ContratDto.Request contrat);
    void deleteById(Long id);
}