package com.example.hotelmanagertool.service;

import com.example.hotelmanagertool.DTO.ReservationDTO;
import com.example.hotelmanagertool.DTO.ReservationDetailsDTO;
import com.example.hotelmanagertool.entity.ChambreEntity;
import com.example.hotelmanagertool.entity.ReservationEntity;
import com.example.hotelmanagertool.entity.UtilsateurEntity;
import com.example.hotelmanagertool.entity.enums.ChambreTypeEnum;
import com.example.hotelmanagertool.repository.ReservationRepository;
import com.example.hotelmanagertool.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ChambreService chambreService;
    private final UtilisateurRepository utilisateurRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,ChambreService chambreService,UtilisateurRepository utilisateurRepository,JavaMailSender javaMailSender) {
        this.reservationRepository = reservationRepository;
        this.chambreService=chambreService;
        this.utilisateurRepository=utilisateurRepository;
        this.mailSender=javaMailSender;
    }

    public Page<ReservationEntity> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    public List<ChambreEntity> getAvailableRooms(ChambreTypeEnum category, LocalDate startDate, LocalDate endDate) {
        List<ChambreEntity> allRooms = this.chambreService.getAllChambres();
        List<ReservationEntity> reservationEntities = this.reservationRepository.findByDateFinAfterAndDateDebutBefore(startDate, endDate);
        System.out.println("reservations "+reservationEntities);
        List<ChambreEntity> reservedRooms = reservationEntities.stream().map(ReservationEntity::getChambre).collect(Collectors.toList());

        List<ChambreEntity> allAvailableRooms = allRooms.stream()
                .filter(chambreEntity -> !reservedRooms.contains(chambreEntity))
                .collect(Collectors.toList());

        List<ChambreEntity> availableRooms = allAvailableRooms.stream()
                .filter(chambreEntity -> chambreEntity.getCategorie() == category)
                .collect(Collectors.toList());

        System.out.println(availableRooms.toString());
        return availableRooms;
    }


    public ReservationEntity makeReservation(ReservationDTO reservationDTO) throws Exception {
        Optional<ChambreEntity> availableRoom = this.chambreService.getChambreById(reservationDTO.getChambreId());
        if(availableRoom.isEmpty()){
            throw new Exception("Cannot find a room with this data");
        }
       ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setChambre(availableRoom.get());
        reservationEntity.setDateDebut(reservationDTO.getStartDate());
        reservationEntity.setDateFin(reservationDTO.getEndDate());
        reservationEntity.setCoutTotal(priceReservation(reservationDTO));
        Optional<UtilsateurEntity> utilsateur = this.utilisateurRepository.findById(reservationDTO.getIdUtilisateur());
        if(!utilsateur.isPresent()){
            throw new Exception("Cannot find this user");
        }
        reservationEntity.setUtilisateur(utilsateur.get());
        String bodyMail = "Dear "+ utilsateur.get().getPrenom()+ ",\n" +
                "\n" +
                "I hope this message finds you well. We would like to express our sincere gratitude for choosing Hotel La Cigale for your upcoming stay.\n" +
                "\n" +
                "It is a true pleasure to welcome you to our establishment, and we are delighted that you have selected our hotel for your accommodation. We are committed to ensuring that your stay is as enjoyable as possible.\n" +
                "\n" +
                "Your reservation has been confirmed for the dates from "+reservationDTO.getStartDate() + " to "+ reservationDTO.getEndDate()+", in the "+reservationDTO.getCategory() + " room type. If you have any special requests or preferences you would like to share with us, please feel free to let us know. We are here to ensure that your stay meets your expectations.\n" +
                "\n" +
                "Should you have any additional questions or require specific assistance before your arrival, do not hesitate to contact us. We are here to assist you in making your experience at Hotel La Cigale memorable.\n" +
                "\n" +
                "We look forward to welcoming you soon and making your stay an unforgettable one.\n" +
                "\n" +
                "Once again, thank you for choosing us. We are honored to have you as our valued guest.\n" +
                "\n" +
                "Best regards,";
        this.sendEmail(utilsateur.get().getEmail(),"Thank You for Your Reservation at Hotel La Cigale",bodyMail);
        Set<ReservationEntity> reservation = availableRoom.get().getReservations();
        reservation.add(reservationEntity);
        availableRoom.get().setReservations(reservation);
        this.chambreService.saveChambre(availableRoom.get());
        return this.reservationRepository.save(reservationEntity);
    }
    public Double priceReservation(ReservationDTO reservationDTO){
      double basicPrice=85000;
      Map<String, Double> seasonMap= new HashMap<>();
      seasonMap.put("HOT_SEASON",1.75);
      seasonMap.put("MID_SEASON",1.4);
      seasonMap.put("NORMAL_SEASON",1.0);
      Map<ChambreTypeEnum,Double> categoryMap= new HashMap<>();
      categoryMap.put(ChambreTypeEnum.SIMPLE,1.0);
      categoryMap.put(ChambreTypeEnum.DOUBLE,2.0);
      categoryMap.put(ChambreTypeEnum.TRIPLE,2.8);
      categoryMap.put(ChambreTypeEnum.QUADRIPLE,3.6);
      String season;
      switch (reservationDTO.getStartDate().getMonth()){
          case MAY:
          case JULY:
          case JUNE:
          case AUGUST:
          case SEPTEMBER: season="HOT_SEASON";break;
          case DECEMBER:
          case JANUARY: season="MID_SEASON"; break;
          default:season="NORMAL_SEASON";
      }
      System.out.println(season+ ' ' +seasonMap.get(season));
      int days= reservationDTO.getEndDate().getDayOfMonth()-reservationDTO.getStartDate().getDayOfMonth();
      return  Math.ceil(basicPrice*seasonMap.get(season)*categoryMap.get(reservationDTO.getCategory())*days);
    }
    public ReservationDetailsDTO simulateReservation(ReservationDTO reservationDTO) throws Exception {
        Optional<ChambreEntity> availableRoom = this.chambreService.getChambreById(reservationDTO.getChambreId());
        if(availableRoom==null){
            throw new Exception("Cannot find a room with this data");
        }
        ReservationDetailsDTO reservationDTO1 = new ReservationDetailsDTO();
        reservationDTO1.setStartDate(reservationDTO.getStartDate());
        reservationDTO1.setEndDate(reservationDTO.getEndDate());
        reservationDTO1.setCategory(reservationDTO.getCategory());
        reservationDTO1.setPrice(priceReservation(reservationDTO));
        reservationDTO1.setChambreId(reservationDTO.getChambreId());
        reservationDTO1.setIsBeachViewAvailable(availableRoom.get().getIsBeachViewAvailable());
        reservationDTO1.setIsWifiAvailable(availableRoom.get().getIsWifiAvailable());
        return reservationDTO1;
    }
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    public List<ReservationEntity> getUserReservations(Long utilisateurId) {
        return reservationRepository.findByUtilisateurId(utilisateurId);
    }
}
