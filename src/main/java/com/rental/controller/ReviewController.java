package com.rental.controller;

import com.rental.dto.ReviewRequest;
import com.rental.entity.Review;
import com.rental.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Review>> getByVehicle(@PathVariable Integer vehicleId) {
        return ResponseEntity.ok(reviewService.getReviewsByVehicle(vehicleId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Review>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(reviewService.getReviewsByCustomer(customerId));
    }

    @PostMapping
    public ResponseEntity<?> submitReview(@Valid @RequestBody ReviewRequest request) {
        try {
            Review review = reviewService.submitReview(request);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
