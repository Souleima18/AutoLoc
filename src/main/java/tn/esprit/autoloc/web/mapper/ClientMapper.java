package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.web.dto.ClientDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientMapper {

    @Mapping(target = "idClient", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Client toEntity(ClientDto.Request request);

    @Mapping(target = "idClient", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    void updateEntity(ClientDto.Request request, @MappingTarget Client client);

    ClientDto.Response toResponse(Client client);
}
