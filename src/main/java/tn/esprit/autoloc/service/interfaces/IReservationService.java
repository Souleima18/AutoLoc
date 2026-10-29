package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.ReservationDto;

public interface IReservationService {
    ReservationDto.Response create(ReservationDto.Request reservation);
    ReservationDto.Response findById(Long id);
    List<ReservationDto.Response> findAll();
    ReservationDto.Response update(Long id, ReservationDto.Request reservation);
    void deleteById(Long id);
}