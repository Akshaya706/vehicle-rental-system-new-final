package com.rental.service;

import com.rental.dto.ReviewRequest;
import com.rental.entity.Review;
import com.rental.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByReviewIdDesc();
    }

    public List<Review> getReviewsByVehicle(Integer vehicleId) {
        return reviewRepository.findByVehicleIdOrderByReviewIdDesc(vehicleId);
    }

    public List<Review> getReviewsByCustomer(Integer customerId) {
        return reviewRepository.findByCustomerIdOrderByReviewIdDesc(customerId);
    }

    @Transactional
    public Review submitReview(ReviewRequest request) {
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars");
        }

        Review review = new Review(
                request.getCustomerId(),
                request.getVehicleId(),
                request.getBookingId(),
                request.getRating(),
                request.getComment()
        );

        return reviewRepository.save(review);
    }
}
