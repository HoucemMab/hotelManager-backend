package com.example.hotelmanagertool.controller;

import com.example.hotelmanagertool.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/generate")
    public ResponseEntity<String> generatePayment(@RequestParam String amount) {
        return paymentService.generatePayment(amount);
    }
    @GetMapping("/verify/{id}")
    public ResponseEntity<String> verifyPayment(@PathVariable String id){
        return paymentService.verifyPayment(id);
    }

}
