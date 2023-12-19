package com.example.hotelmanagertool.DTO;

import com.example.hotelmanagertool.entity.enums.ChambreTypeEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDetailsDTO {
    private ChambreTypeEnum category;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isWifiAvailable;
    private Boolean isBeachViewAvailable;
}
