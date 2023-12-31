package com.example.hotelmanagertool.service;


import com.example.hotelmanagertool.DTO.AuthRequest;
import com.example.hotelmanagertool.DTO.UserDTO;
import com.example.hotelmanagertool.config.JwtTokenUtil;
import com.example.hotelmanagertool.entity.UtilsateurEntity;
import com.example.hotelmanagertool.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurService implements UserDetailsService {
    @Autowired
    private UtilisateurRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UtilsateurEntity register(UtilsateurEntity utilsateur){
        utilsateur.setMotDePasse(passwordEncoder.encode(utilsateur.getMotDePasse()));
        System.out.println(utilsateur.getMotDePasse());
        return this.userRepository.save(utilsateur);
    }
    public String login (AuthRequest authRequest){
        System.out.println(authRequest.getEmail() +" " + authRequest.getPassword());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        var user= userRepository.findUtilsateurEntityByEmail(authRequest.getEmail()).orElseThrow();
        System.out.println(user.toString());
        var jwtToken = jwtTokenUtil.generateToken(user.getEmail(),user.getRole(),user.getId(), user.getPrenom());
        System.out.println("token" + jwtToken);
        return  jwtToken;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Implement loading user details from the repository
        Optional<UtilsateurEntity> user = userRepository.findUtilsateurEntityByEmail(email);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found with this email: " + email);
        }

        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(),
                user.get().getMotDePasse(),
                Collections.emptyList() // You may add user roles here
        );
    }
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<UtilsateurEntity> usersPage = this.userRepository.findAll(pageable);
        List<UserDTO> userDTOList = usersPage.getContent()
                .stream()
                .map(utilsateur -> new UserDTO(utilsateur.getId(), utilsateur.getNom(), utilsateur.getPrenom(), utilsateur.getEmail()))
                .collect(Collectors.toList());
        // PageImpl will create this sub page and return it
        return new PageImpl<>(userDTOList, usersPage.getPageable(), usersPage.getTotalElements());
    }

}
