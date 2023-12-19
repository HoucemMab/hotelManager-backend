package com.example.hotelmanagertool.service;

import com.example.hotelmanagertool.entity.ChambreEntity;
import com.example.hotelmanagertool.repository.ChambreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChambreService {
    private final ChambreRepository chambreRepository;

    @Autowired
    public ChambreService(ChambreRepository chambreRepository) {
        this.chambreRepository = chambreRepository;
    }

    public List<ChambreEntity> getAllChambres() {
        return chambreRepository.findAll();
    }

    public Optional<ChambreEntity> getChambreById(Long id) {
        return chambreRepository.findById(id);
    }

    public ChambreEntity saveChambre(ChambreEntity chambre) {
        return chambreRepository.save(chambre);
    }

    public void deleteChambre(Long id) {
        chambreRepository.deleteById(id);
    }


}
