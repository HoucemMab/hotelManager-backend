package com.example.hotelmanagertool.repository;

import com.example.hotelmanagertool.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository  extends JpaRepository<ReservationEntity,Long> {
     List<ReservationEntity> findReservationEntitiesByDateDebutAfterAndDateFinBefore(LocalDate dateDebut, LocalDate dateFin);
}
