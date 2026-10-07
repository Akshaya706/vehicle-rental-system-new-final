package com.rental.repository;

import com.rental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByCustomerIdOrderByBookingIdDesc(Integer customerId);
    List<Booking> findByVehicleId(Integer vehicleId);
    List<Booking> findByBookingStatus(String bookingStatus);
    List<Booking> findAllByOrderByBookingIdDesc();

    // Prevent overlapping bookings (SRS Constraint 8)
    @Query("SELECT b FROM Booking b WHERE b.vehicleId = :vehicleId " +
           "AND b.bookingStatus NOT IN ('CANCELLED', 'REJECTED') " +
           "AND (:excludeBookingId IS NULL OR b.bookingId != :excludeBookingId) " +
           "AND (b.rentalStartDate <= :endDate AND b.rentalEndDate >= :startDate)")
    List<Booking> findOverlappingBookings(@Param("vehicleId") Integer vehicleId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("excludeBookingId") Integer excludeBookingId);

    long countByBookingStatus(String bookingStatus);
}
