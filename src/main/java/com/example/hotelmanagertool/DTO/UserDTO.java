package com.example.hotelmanagertool.DTO;

import com.example.hotelmanagertool.entity.ReservationEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id ;
    private String nom;
    private String prenom;
    private String email;
    private Long nbReservations;
    public UserDTO(Long id , String nom , String prenom,String email){
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.email=email;
    }
}
