package com.example.hotelmanagertool.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class UtilsateurEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String Role;
    @OneToMany(mappedBy = "utilisateur")
    private Set<ReservationEntity> reservations;

}
