package com.rental.controller;

import com.rental.dto.BookingRequest;
import com.rental.dto.PriceCalculationRequest;
import com.rental.dto.PriceCalculationResponse;
import com.rental.entity.Booking;
import com.rental.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/calculate-price")
    public ResponseEntity<?> calculatePrice(@RequestBody PriceCalculationRequest request) {
        try {
            PriceCalculationResponse resp = bookingService.calculatePrice(request);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {
        try {
            Booking booking = bookingService.createBooking(request);
            return ResponseEntity.ok(booking);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(@RequestParam(required = false) Integer customerId,
                                                    @RequestParam(required = false) String status) {
        if (customerId != null) {
            return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId));
        }
        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
        }
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveBooking(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String notes = body != null ? body.get("notes") : null;
            Booking booking = bookingService.approveBooking(id, notes);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectBooking(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String reason = body != null ? body.get("reason") : "Manager rejected request";
            Booking booking = bookingService.rejectBooking(id, reason);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/handover")
    public ResponseEntity<?> handoverBooking(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String notes = body != null ? body.get("notes") : "Vehicle handed over to customer";
            Booking booking = bookingService.handoverVehicle(id, notes);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String reason = body != null ? body.get("reason") : "Cancelled by user";
            Booking booking = bookingService.cancelBooking(id, reason);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/pay-final")
    public ResponseEntity<?> payFinalCharges(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String paymentMethod = body != null ? body.getOrDefault("paymentMethod", "Front Desk Settlement") : "Front Desk Settlement";
            Booking booking = bookingService.payFinalCharges(id, paymentMethod);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
