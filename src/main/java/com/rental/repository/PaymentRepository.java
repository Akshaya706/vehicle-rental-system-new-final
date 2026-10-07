package com.rental.repository;

import com.rental.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByBookingId(Integer bookingId);
    Optional<Payment> findFirstByBookingIdOrderByPaymentIdDesc(Integer bookingId);
    List<Payment> findAllByOrderByPaymentIdDesc();

    @Query("SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p WHERE p.paymentStatus = 'PAID'")
    Double getTotalRevenue();
}
