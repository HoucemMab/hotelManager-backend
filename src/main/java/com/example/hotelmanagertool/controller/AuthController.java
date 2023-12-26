package com.example.hotelmanagertool.controller;

import com.example.hotelmanagertool.DTO.AuthRequest;
import com.example.hotelmanagertool.DTO.UserDTO;
import com.example.hotelmanagertool.entity.UtilsateurEntity;
import com.example.hotelmanagertool.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private UtilisateurService authService;

    @PostMapping("/addUser")
    public ResponseEntity<UtilsateurEntity> addUser(@RequestBody UtilsateurEntity user) {
        System.out.println(user.getMotDePasse());
        UtilsateurEntity registeredUser = this.authService.register(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        String token = this.authService.login(authRequest);
        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/users")
    public List<UserDTO> getAllUsers(){
        return this.authService.getAllUsers();
    }
}
