package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.web.dto.EmployeDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EmployeMapper {

    @Mapping(target = "idEmploye", ignore = true)
    @Mapping(target = "agence", ignore = true)
    Employe toEntity(EmployeDto.Request request);

    @Mapping(target = "idEmploye", ignore = true)
    @Mapping(target = "agence", ignore = true)
    void updateEntity(EmployeDto.Request request, @MappingTarget Employe employe);

    @Mapping(source = "agence.idAgence", target = "agenceId")
    EmployeDto.Response toResponse(Employe employe);
}
