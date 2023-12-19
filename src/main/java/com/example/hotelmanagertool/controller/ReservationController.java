package com.example.hotelmanagertool.controller;

import com.example.hotelmanagertool.DTO.ReservationDTO;
import com.example.hotelmanagertool.DTO.ReservationDetailsDTO;
import com.example.hotelmanagertool.entity.ChambreEntity;
import com.example.hotelmanagertool.entity.ReservationEntity;
import com.example.hotelmanagertool.entity.enums.ChambreTypeEnum;
import com.example.hotelmanagertool.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @GetMapping
    public List<ReservationEntity> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/available")
    public List<ChambreEntity> getAvailableRooms(
            @RequestParam("category") ChambreTypeEnum category,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {

        return this.reservationService.getAvailableRooms(category,startDate,endDate);
    }

    @PostMapping("/reserve")
    public ReservationEntity makeReservation(@RequestBody ReservationDTO reservation) throws Exception{
        return reservationService.makeReservation(reservation);
    }
    @PostMapping("/simulate")
    public ReservationDetailsDTO simulateReservation(@RequestBody ReservationDTO reservationDTO) throws Exception {
        return  reservationService.simulateReservation(reservationDTO);
    }
}
