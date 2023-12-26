package com.example.hotelmanagertool.repository;

import com.example.hotelmanagertool.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    // Custom queries, if needed
}
