package com.example.hotelmanagertool.controller;


import com.example.hotelmanagertool.entity.ChambreEntity;
import com.example.hotelmanagertool.service.ChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/chambres")
public class ChambreController {
    private final ChambreService chambreService;

    @Autowired
    public ChambreController(ChambreService chambreService) {
        this.chambreService = chambreService;
    }

    @GetMapping
    public List<ChambreEntity> getAllChambres() {
        return chambreService.getAllChambres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChambreEntity> getChambreById(@PathVariable Long id) {
        return chambreService.getChambreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ChambreEntity> saveChambre(@RequestBody ChambreEntity chambre) {
        ChambreEntity savedChambre = chambreService.saveChambre(chambre);
        return ResponseEntity.ok(savedChambre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChambre(@PathVariable Long id) {
        chambreService.deleteChambre(id);
        return ResponseEntity.noContent().build();
    }

}
