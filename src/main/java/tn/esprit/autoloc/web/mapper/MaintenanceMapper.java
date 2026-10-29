package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.web.dto.MaintenanceDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MaintenanceMapper {

    @Mapping(target = "idMaintenance", ignore = true)
    @Mapping(target = "vehicule", ignore = true)
    Maintenance toEntity(MaintenanceDto.Request request);

    @Mapping(target = "idMaintenance", ignore = true)
    @Mapping(target = "vehicule", ignore = true)
    void updateEntity(MaintenanceDto.Request request, @MappingTarget Maintenance maintenance);

    @Mapping(source = "vehicule.idVehicule", target = "vehiculeId")
    MaintenanceDto.Response toResponse(Maintenance maintenance);
}
