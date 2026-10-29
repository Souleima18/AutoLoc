package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.AgenceDto;

public interface IAgenceService {
    AgenceDto.Response create(AgenceDto.Request agence);
    AgenceDto.Response findById(Long id);
    List<AgenceDto.Response> findAll();
    AgenceDto.Response update(Long id, AgenceDto.Request agence);
    void deleteById(Long id);
}