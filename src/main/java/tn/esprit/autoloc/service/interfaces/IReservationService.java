package tn.esprit.autoloc.service.interfaces;

import java.util.List;
import tn.esprit.autoloc.web.dto.ReservationDto;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;

public interface IReservationService {
    ReservationDto.Response create(ReservationDto.Request reservation);
    ReservationDto.Response findById(Long id);
    List<ReservationDto.Response> findAll();
    ReservationDto.Response update(Long id, ReservationDto.Request reservation);
    void deleteById(Long id);
    ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom);
    ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO);
}