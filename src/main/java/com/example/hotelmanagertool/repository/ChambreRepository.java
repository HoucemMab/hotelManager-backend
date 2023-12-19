package com.example.hotelmanagertool.repository;

import com.example.hotelmanagertool.entity.ChambreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChambreRepository extends JpaRepository<ChambreEntity,Long> {
}
