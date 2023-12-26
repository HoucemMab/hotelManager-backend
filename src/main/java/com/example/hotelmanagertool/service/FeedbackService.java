package com.example.hotelmanagertool.service;

import com.example.hotelmanagertool.entity.FeedbackEntity;
import com.example.hotelmanagertool.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public FeedbackEntity addFeedback(FeedbackEntity feedback) {
        // Set the creation timestamp before saving
        feedback.setCreatedAt(new Date());
        return feedbackRepository.save(feedback);
    }

    public List<FeedbackEntity> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

}
