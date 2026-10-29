package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReservationMapper {

    @Mapping(target = "idReservation", ignore = true)
    @Mapping(target = "vehicule", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "contrat", ignore = true)
    Reservation toEntity(ReservationDto.Request request);

    @Mapping(target = "idReservation", ignore = true)
    @Mapping(target = "vehicule", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "contrat", ignore = true)
    void updateEntity(ReservationDto.Request request, @MappingTarget Reservation reservation);

    @Mapping(source = "vehicule.idVehicule", target = "vehiculeId")
    @Mapping(source = "client.idClient", target = "clientId")
    @Mapping(source = "contrat.idContrat", target = "contratId")
    ReservationDto.Response toResponse(Reservation reservation);
}
