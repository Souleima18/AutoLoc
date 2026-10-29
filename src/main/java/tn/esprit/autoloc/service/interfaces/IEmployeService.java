package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.EmployeDto;

public interface IEmployeService {
    EmployeDto.Response create(EmployeDto.Request employe);
    EmployeDto.Response findById(Long id);
    List<EmployeDto.Response> findAll();
    EmployeDto.Response update(Long id, EmployeDto.Request employe);
    void deleteById(Long id);
}