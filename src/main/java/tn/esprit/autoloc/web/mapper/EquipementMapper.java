package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.web.dto.EquipementDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EquipementMapper {

    @Mapping(target = "idEquipement", ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    Equipement toEntity(EquipementDto.Request request);

    @Mapping(target = "idEquipement", ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    void updateEntity(EquipementDto.Request request, @MappingTarget Equipement equipement);

    EquipementDto.Response toResponse(Equipement equipement);
}
