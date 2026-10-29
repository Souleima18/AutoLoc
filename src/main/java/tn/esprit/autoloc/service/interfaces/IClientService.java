package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.ClientDto;

public interface IClientService {
    ClientDto.Response create(ClientDto.Request client);
    ClientDto.Response findById(Long id);
    List<ClientDto.Response> findAll();
    ClientDto.Response update(Long id, ClientDto.Request client);
    void deleteById(Long id);
}