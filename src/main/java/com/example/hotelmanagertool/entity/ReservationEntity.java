package com.example.hotelmanagertool.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chambre_id")
    private ChambreEntity chambre;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private UtilsateurEntity utilisateur;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double coutTotal;
}
