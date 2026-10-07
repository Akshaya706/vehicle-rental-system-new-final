package com.rental.service;

import com.rental.dto.PaymentRequest;
import com.rental.entity.Booking;
import com.rental.entity.Payment;
import com.rental.entity.Vehicle;
import com.rental.repository.BookingRepository;
import com.rental.repository.PaymentRepository;
import com.rental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentIdDesc();
    }

    public List<Payment> getPaymentsByBooking(Integer bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    @Transactional
    public Payment processPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + request.getBookingId()));

        if ("PAID".equalsIgnoreCase(booking.getPaymentStatus()) &&
            !"PENDING_FINAL_PAYMENT".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalStateException("Payment already received for this booking.");
        }

        if (!"APPROVED".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalStateException("Cannot process upfront payment: Booking must be APPROVED by Rental Manager first.");
        }

        double payAmount = (request.getAmount() != null && request.getAmount() > 0) ? 
                request.getAmount() : booking.getTotalCharge();

        String txRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = new Payment(
                booking.getBookingId(),
                request.getPaymentMethod(),
                payAmount,
                "PAID",
                txRef
        );
        payment = paymentRepository.save(payment);

        // Update booking: paid and becomes ACTIVE (Vehicle Handover per SRS Constraint 8)
        booking.setPaymentStatus("PAID");
        booking.setBookingStatus("ACTIVE");
        bookingRepository.save(booking);

        // Vehicle is now officially on the road / RENTED
        Vehicle vehicle = null;
        if (booking.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(booking.getVehicleId()).orElse(null);
        }
        if (vehicle != null) {
            vehicle.setAvailability("RENTED");
            vehicleRepository.save(vehicle);
            booking.setVehicle(vehicle);
        }

        return payment;
    }

    public Map<String, Object> generateInvoice(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        Payment payment = paymentRepository.findFirstByBookingIdOrderByPaymentIdDesc(bookingId).orElse(null);

        Map<String, Object> invoice = new LinkedHashMap<>();
        invoice.put("invoiceNumber", "INV-" + (10000 + booking.getBookingId()));
        invoice.put("invoiceDate", payment != null ? payment.getPaymentDate() : LocalDate.now());
        invoice.put("bookingId", booking.getBookingId());
        invoice.put("bookingStatus", booking.getBookingStatus());
        invoice.put("paymentStatus", booking.getPaymentStatus());
        invoice.put("paymentMethod", payment != null ? payment.getPaymentMethod() : "Pending");
        invoice.put("transactionRef", payment != null ? payment.getTransactionRef() : "N/A");

        if (booking.getCustomer() != null) {
            Map<String, Object> custInfo = new LinkedHashMap<>();
            custInfo.put("customerId", booking.getCustomer().getCustomerId());
            custInfo.put("name", booking.getCustomer().getName());
            custInfo.put("phone", booking.getCustomer().getPhone());
            custInfo.put("address", booking.getCustomer().getAddress());
            custInfo.put("drivingLicence", booking.getCustomer().getDrivingLicence());
            invoice.put("customer", custInfo);
        }

        if (booking.getVehicle() != null) {
            Map<String, Object> vehInfo = new LinkedHashMap<>();
            vehInfo.put("vehicleId", booking.getVehicle().getVehicleId());
            vehInfo.put("title", booking.getVehicle().getBrand() + " " + booking.getVehicle().getModel());
            vehInfo.put("registrationNo", booking.getVehicle().getRegistrationNo());
            vehInfo.put("type", booking.getVehicle().getVehicleType());
            invoice.put("vehicle", vehInfo);
        }

        invoice.put("rentalStartDate", booking.getRentalStartDate());
        invoice.put("rentalEndDate", booking.getRentalEndDate());
        invoice.put("rentalDays", booking.getRentalDays());
        invoice.put("baseDailyRate", booking.getBaseDailyRate());
        invoice.put("dynamicMultiplier", booking.getDynamicMultiplier());
        invoice.put("extraCharges", booking.getExtraCharges());
        invoice.put("lateFee", booking.getLateFee());
        invoice.put("damageFee", booking.getDamageFee());
        invoice.put("totalAmount", booking.getTotalCharge());

        return invoice;
    }
}
