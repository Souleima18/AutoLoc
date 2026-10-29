package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.MaintenanceDto;

public interface IMaintenanceService {
    MaintenanceDto.Response create(MaintenanceDto.Request maintenance);
    MaintenanceDto.Response findById(Long id);
    List<MaintenanceDto.Response> findAll();
    MaintenanceDto.Response update(Long id, MaintenanceDto.Request maintenance);
    void deleteById(Long id);
}