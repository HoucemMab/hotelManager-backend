package com.example.hotelmanagertool.service;

import com.example.hotelmanagertool.DTO.ReservationDTO;
import com.example.hotelmanagertool.DTO.ReservationDetailsDTO;
import com.example.hotelmanagertool.entity.ChambreEntity;
import com.example.hotelmanagertool.entity.ReservationEntity;
import com.example.hotelmanagertool.entity.UtilsateurEntity;
import com.example.hotelmanagertool.entity.enums.ChambreTypeEnum;
import com.example.hotelmanagertool.repository.ReservationRepository;
import com.example.hotelmanagertool.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ChambreService chambreService;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,ChambreService chambreService,UtilisateurRepository utilisateurRepository) {
        this.reservationRepository = reservationRepository;
        this.chambreService=chambreService;
        this.utilisateurRepository=utilisateurRepository;
    }

    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<ChambreEntity> getAvailableRooms(ChambreTypeEnum category, LocalDate startDate, LocalDate endDate) {
        List<ChambreEntity> allRooms = this.chambreService.getAllChambres();
        List<ReservationEntity> reservationEntities = this.reservationRepository.findReservationEntitiesByDateDebutAfterAndDateFinBefore(startDate,endDate);
        List<ChambreEntity> reservedRooms = reservationEntities.stream().map(ReservationEntity::getChambre).collect(Collectors.toList());
        List<ChambreEntity> allAvailableRooms = allRooms.stream().filter(chambreEntity -> reservedRooms.indexOf(chambreEntity)== -1).collect(Collectors.toList());
        List<ChambreEntity>  availableRooms = allAvailableRooms.stream().filter(chambreEntity -> chambreEntity.getCategorie()==category).collect(Collectors.toList());
        return availableRooms;
    }

    public ReservationEntity makeReservation(ReservationDTO reservationDTO) throws Exception {
        List<ChambreEntity> availableRooms = this.getAvailableRooms(reservationDTO.getCategory(),reservationDTO.getStartDate(),reservationDTO.getEndDate());
        if(availableRooms.size()==0){
            throw new Exception("Cannot find a room with this data");
        }
       ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setChambre(availableRooms.get(0));
        reservationEntity.setDateDebut(reservationDTO.getStartDate());
        reservationEntity.setDateFin(reservationDTO.getEndDate());
        reservationEntity.setCoutTotal(priceReservation(reservationDTO));
        Optional<UtilsateurEntity> utilsateur = this.utilisateurRepository.findById(reservationDTO.getIdUtilisateur());
        if(!utilsateur.isPresent()){
            throw new Exception("Cannot find this user");
        }
        reservationEntity.setUtilisateur(utilsateur.get());
        return this.reservationRepository.save(reservationEntity);
    }
    public Double priceReservation(ReservationDTO reservationDTO){
      double basicPrice=80.5;
      Map<String, Double> seasonMap= new HashMap<>();
      seasonMap.put("HOT_SEASON",1.75);
      seasonMap.put("MID_SEASON",1.4);
      seasonMap.put("NORMAL_SEASON",1.0);
      Map<ChambreTypeEnum,Double> categoryMap= new HashMap<>();
      categoryMap.put(ChambreTypeEnum.SIMPLE,1.0);
      categoryMap.put(ChambreTypeEnum.DOUBLE,2.0);
      categoryMap.put(ChambreTypeEnum.TRIPLE,2.8);
      categoryMap.put(ChambreTypeEnum.QUADRIPLE,3.6);
      String season;
      switch (reservationDTO.getStartDate().getMonth()){
          case MAY:
          case JULY:
          case JUNE:
          case AUGUST:
          case SEPTEMBER: season="HOT_SEASON";break;
          case DECEMBER:
          case JANUARY: season="MID_SEASON"; break;
          default:season="NORMAL_SEASON";
      }
      return basicPrice*seasonMap.get(season)*categoryMap.get(reservationDTO.getCategory());
    }
    public ReservationDetailsDTO simulateReservation(ReservationDTO reservationDTO) throws Exception {
        List<ChambreEntity> availableRooms = this.getAvailableRooms(reservationDTO.getCategory(),reservationDTO.getStartDate(),reservationDTO.getEndDate());
        if(availableRooms.size()==0){
            throw new Exception("Cannot find a room with this data");
        }
        ReservationDetailsDTO reservationDTO1 = new ReservationDetailsDTO();
        reservationDTO1.setStartDate(reservationDTO.getStartDate());
        reservationDTO1.setEndDate(reservationDTO.getEndDate());
        reservationDTO1.setCategory(reservationDTO.getCategory());
        reservationDTO1.setPrice(priceReservation(reservationDTO));
        return reservationDTO1;
    }
}
