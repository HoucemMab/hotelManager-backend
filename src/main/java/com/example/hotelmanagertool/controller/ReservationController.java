package com.example.hotelmanagertool.controller;

import com.example.hotelmanagertool.DTO.ReservationDTO;
import com.example.hotelmanagertool.DTO.ReservationDetailsDTO;
import com.example.hotelmanagertool.entity.ChambreEntity;
import com.example.hotelmanagertool.entity.ReservationEntity;
import com.example.hotelmanagertool.entity.enums.ChambreTypeEnum;
import com.example.hotelmanagertool.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
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
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationEntity>> getUserReservations(@PathVariable Long userId) {
        List<ReservationEntity> reservations = reservationService.getUserReservations(userId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ReservationEntity makeReservation(@RequestBody ReservationDTO reservation) throws Exception{
        System.out.println(reservation.toString());
        return reservationService.makeReservation(reservation);
    }
    @PostMapping("/simulate")
    public ReservationDetailsDTO simulateReservation(@RequestBody ReservationDTO reservationDTO) throws Exception {
        return  reservationService.simulateReservation(reservationDTO);
    }
}
