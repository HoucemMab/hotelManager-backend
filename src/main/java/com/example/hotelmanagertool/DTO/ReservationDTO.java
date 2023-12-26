package com.example.hotelmanagertool.DTO;

import com.example.hotelmanagertool.entity.enums.ChambreTypeEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private ChambreTypeEnum category;
    private Long chambreId;
    private Long idUtilisateur;
}
