package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.web.dto.ContratDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ContratMapper {

    @Mapping(target = "idContrat", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "paiements", ignore = true)
    Contrat toEntity(ContratDto.Request request);

    @Mapping(target = "idContrat", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "paiements", ignore = true)
    void updateEntity(ContratDto.Request request, @MappingTarget Contrat contrat);

    ContratDto.Response toResponse(Contrat contrat);
}
