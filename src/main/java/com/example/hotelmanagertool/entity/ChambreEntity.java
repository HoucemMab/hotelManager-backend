package com.example.hotelmanagertool.entity;

import com.example.hotelmanagertool.entity.enums.ChambreTypeEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class ChambreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ChambreTypeEnum categorie;
    private Boolean isWifiAvailable;
    private Boolean isBeachViewAvailable;

    @OneToMany(mappedBy = "chambre")
    private Set<ReservationEntity> reservations;

}
