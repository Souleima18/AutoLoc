package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VehiculeMapper {

    @Mapping(target = "idVehicule", ignore = true)
    @Mapping(target = "agence", ignore = true)
    @Mapping(target = "maintenances", ignore = true)
    @Mapping(target = "equipements", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Vehicule toEntity(VehiculeDto.Request request);

    @Mapping(target = "idVehicule", ignore = true)
    @Mapping(target = "agence", ignore = true)
    @Mapping(target = "maintenances", ignore = true)
    @Mapping(target = "equipements", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    void updateEntity(VehiculeDto.Request request, @MappingTarget Vehicule vehicule);

    @Mapping(source = "agence.idAgence", target = "agenceId")
    VehiculeDto.Response toResponse(Vehicule vehicule);
}
