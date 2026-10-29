package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AgenceMapper {

    @Mapping(target = "idAgence", ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    @Mapping(target = "employes", ignore = true)
    Agence toEntity(AgenceDto.Request request);

    @Mapping(target = "idAgence", ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    @Mapping(target = "employes", ignore = true)
    void updateEntity(AgenceDto.Request request, @MappingTarget Agence agence);

    AgenceDto.Response toResponse(Agence agence);
}
