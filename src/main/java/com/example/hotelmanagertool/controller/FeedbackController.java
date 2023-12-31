package com.example.hotelmanagertool.controller;

import com.example.hotelmanagertool.entity.FeedbackEntity;
import com.example.hotelmanagertool.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/add")
    public ResponseEntity<FeedbackEntity> addFeedback(@RequestBody FeedbackEntity feedback) {
        FeedbackEntity savedFeedback = feedbackService.addFeedback(feedback);
        return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<FeedbackEntity>> getAllFeedbacks(Pageable pageable) {
        Page<FeedbackEntity> page = feedbackService.getAllFeedbacks(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
