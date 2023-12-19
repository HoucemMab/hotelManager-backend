package com.example.hotelmanagertool.repository;

import com.example.hotelmanagertool.entity.UtilsateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository  extends JpaRepository<UtilsateurEntity,Long> {
}
