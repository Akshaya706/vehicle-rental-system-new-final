package com.rental.service;

import com.rental.dto.BookingRequest;
import com.rental.dto.PriceCalculationRequest;
import com.rental.dto.PriceCalculationResponse;
import com.rental.entity.Booking;
import com.rental.entity.Payment;
import com.rental.entity.Vehicle;
import com.rental.repository.BookingRepository;
import com.rental.repository.CustomerRepository;
import com.rental.repository.PaymentRepository;
import com.rental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByBookingIdDesc();
    }

    public List<Booking> getBookingsByCustomer(Integer customerId) {
        return bookingRepository.findByCustomerIdOrderByBookingIdDesc(customerId);
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByBookingStatus(status);
    }

    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));
    }

    /**
     * FR6 Dynamic Rental Charge Calculation
     */
    public PriceCalculationResponse calculatePrice(PriceCalculationRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("Rental end date cannot be before start date");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + request.getVehicleId()));

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days <= 0) {
            days = 1; // Minimum 1 day
        }

        double dailyRate = vehicle.getDailyRate();
        double multiplier = 1.0;
        String tierName = "Standard Daily Rate";

        // Check if rental dates span weekend (Friday, Saturday, or Sunday)
        boolean hasWeekend = false;
        LocalDate cur = request.getStartDate();
        while (!cur.isAfter(request.getEndDate())) {
            DayOfWeek dow = cur.getDayOfWeek();
            if (dow == DayOfWeek.FRIDAY || dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
                hasWeekend = true;
                break;
            }
            cur = cur.plusDays(1);
        }

        if (hasWeekend) {
            multiplier = 1.15; // +15% Dynamic Weekend Demand Surcharge
            tierName = "Weekend Dynamic Pricing (+15%)";
        }

        // Long term discount if rental is >= 7 days (-10% base rate)
        if (days >= 7) {
            multiplier = multiplier * 0.90;
            tierName += " [Long-Term 10% Discount Applied]";
        }

        double dynamicRate = Math.round(dailyRate * multiplier * 100.0) / 100.0;
        double baseTotal = Math.round(dynamicRate * days * 100.0) / 100.0;

        // Additional charges:
        // Insurance Waiver: $15/day
        // GPS Navigation: $5/day
        double extras = 0.0;
        if (request.isInsuranceWaiver()) {
            extras += 15.0 * days;
        }
        if (request.isGpsNavigation()) {
            extras += 5.0 * days;
        }

        double grandTotal = Math.round((baseTotal + extras) * 100.0) / 100.0;

        PriceCalculationResponse response = new PriceCalculationResponse();
        response.setRentalDays((int) days);
        response.setDailyRate(dailyRate);
        response.setDynamicRate(dynamicRate);
        response.setDynamicMultiplier(multiplier);
        response.setPricingTierName(tierName);
        response.setBaseTotal(baseTotal);
        response.setExtraCharges(extras);
        response.setTotalRentalCharge(grandTotal);

        return response;
    }

    /**
     * FR4 Vehicle Booking with Constraint Validation
     */
    @Transactional
    public Booking createBooking(BookingRequest request) {
        if (request.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer identification is required to book a vehicle");
        }
        if (request.getRentalStartDate() == null || request.getRentalEndDate() == null) {
            throw new IllegalArgumentException("Rental start and end dates are required");
        }
        if (request.getRentalEndDate().isBefore(request.getRentalStartDate())) {
            throw new IllegalArgumentException("End date cannot be earlier than start date");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + request.getVehicleId()));

        // Constraint 8: "Vehicles under maintenance cannot be booked."
        if ("UNDER_MAINTENANCE".equalsIgnoreCase(vehicle.getAvailability())) {
            throw new IllegalStateException("Booking failed: This vehicle is currently UNDER_MAINTENANCE and cannot be booked.");
        }

        // Constraint 8: "A vehicle cannot have overlapping bookings."
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                vehicle.getVehicleId(), request.getRentalStartDate(), request.getRentalEndDate(), null);
        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Conflict: Vehicle is already booked for overlapping dates (" + 
                    overlapping.get(0).getRentalStartDate() + " to " + overlapping.get(0).getRentalEndDate() + ")");
        }

        // Calculate dynamic charges
        PriceCalculationRequest priceReq = new PriceCalculationRequest();
        priceReq.setVehicleId(vehicle.getVehicleId());
        priceReq.setStartDate(request.getRentalStartDate());
        priceReq.setEndDate(request.getRentalEndDate());
        priceReq.setInsuranceWaiver(request.isInsuranceWaiver());
        priceReq.setGpsNavigation(request.isGpsNavigation());
        PriceCalculationResponse priceResp = calculatePrice(priceReq);

        Booking booking = new Booking(
                request.getCustomerId(),
                vehicle.getVehicleId(),
                request.getRentalStartDate(),
                request.getRentalEndDate(),
                priceResp.getTotalRentalCharge(),
                vehicle.getDailyRate(),
                priceResp.getRentalDays(),
                priceResp.getDynamicMultiplier(),
                priceResp.getExtraCharges()
        );

        if (request.getNotes() != null) {
            booking.setManagerNotes("Customer Note: " + request.getNotes());
        }

        booking.setVehicle(vehicle);
        customerRepository.findById(request.getCustomerId()).ifPresent(booking::setCustomer);

        // Per SRS: Manager approval is required before confirmation
        booking.setBookingStatus("PENDING_APPROVAL");
        booking.setPaymentStatus("PENDING");

        return bookingRepository.save(booking);
    }

    /**
     * Rental Manager Approval & Vehicle Assignment (FR4, FR5)
     */
    @Transactional
    public Booking approveBooking(Integer bookingId, String managerNotes) {
        Booking booking = getBookingById(bookingId);
        if (booking.getBookingStatus() == null || !"PENDING_APPROVAL".equalsIgnoreCase(booking.getBookingStatus().trim())) {
            throw new IllegalStateException("Only bookings in PENDING_APPROVAL status can be approved. Current status: " + booking.getBookingStatus());
        }

        booking.setBookingStatus("APPROVED");
        if (managerNotes != null && !managerNotes.isBlank()) {
            booking.setManagerNotes(managerNotes);
        }

        // Reliably fetch and update vehicle availability to BOOKED
        Vehicle vehicle = null;
        if (booking.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(booking.getVehicleId()).orElse(null);
        }
        if (vehicle != null) {
            vehicle.setAvailability("BOOKED");
            vehicleRepository.save(vehicle);
            booking.setVehicle(vehicle);
        }

        if (booking.getCustomerId() != null && booking.getCustomer() == null) {
            customerRepository.findById(booking.getCustomerId()).ifPresent(booking::setCustomer);
        }

        return bookingRepository.save(booking);
    }

    /**
     * Rental Manager Rejection (FR4)
     */
    @Transactional
    public Booking rejectBooking(Integer bookingId, String reason) {
        Booking booking = getBookingById(bookingId);
        booking.setBookingStatus("REJECTED");
        booking.setManagerNotes("Rejected: " + (reason != null ? reason : "Requirements not met"));

        Vehicle vehicle = null;
        if (booking.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(booking.getVehicleId()).orElse(null);
        }
        if (vehicle != null && "BOOKED".equalsIgnoreCase(vehicle.getAvailability())) {
            vehicle.setAvailability("AVAILABLE");
            vehicleRepository.save(vehicle);
            booking.setVehicle(vehicle);
        }

        return bookingRepository.save(booking);
    }

    /**
     * Vehicle Handover / Dispatch by Manager
     */
    @Transactional
    public Booking handoverVehicle(Integer bookingId, String notes) {
        Booking booking = getBookingById(bookingId);
        if (!"APPROVED".equalsIgnoreCase(booking.getBookingStatus()) && !"PENDING_APPROVAL".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalStateException("Only APPROVED bookings can be dispatched/handed over.");
        }

        booking.setBookingStatus("ACTIVE");
        if ("PENDING".equalsIgnoreCase(booking.getPaymentStatus())) {
            booking.setPaymentStatus("PAID");
            Payment payment = new Payment(booking.getBookingId(), "Front Desk Settlement", booking.getTotalCharge(), "PAID", "TXN-DESK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            paymentRepository.save(payment);
        }

        if (notes != null && !notes.isBlank()) {
            booking.setManagerNotes((booking.getManagerNotes() != null ? booking.getManagerNotes() + " | " : "") + notes);
        }

        Vehicle vehicle = null;
        if (booking.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(booking.getVehicleId()).orElse(null);
        }
        if (vehicle != null) {
            vehicle.setAvailability("RENTED");
            vehicleRepository.save(vehicle);
            booking.setVehicle(vehicle);
        }

        return bookingRepository.save(booking);
    }

    /**
     * Pay pending amount (damage fee + late fee + extra charges) to close the order.
     * Booking must be in PENDING_FINAL_PAYMENT status.
     */
    @Transactional
    public Booking payFinalCharges(Integer bookingId, String paymentMethod) {
        Booking booking = getBookingById(bookingId);
        if (!"PENDING_FINAL_PAYMENT".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalStateException("No pending charges for this booking. Status: " + booking.getBookingStatus());
        }

        double pendingAmount = 0.0;
        if (booking.getDamageFee() != null) pendingAmount += booking.getDamageFee();
        if (booking.getLateFee() != null) pendingAmount += booking.getLateFee();
        if (booking.getExtraCharges() != null) pendingAmount += booking.getExtraCharges();

        if (pendingAmount > 0) {
            String txRef = "TXN-FINAL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Payment payment = new Payment(
                    bookingId,
                    paymentMethod != null ? paymentMethod : "Front Desk Settlement",
                    pendingAmount,
                    "PAID",
                    txRef
            );
            paymentRepository.save(payment);
        }

        // Mark booking as COMPLETED (Order Closed)
        booking.setBookingStatus("COMPLETED");
        booking.setManagerNotes((booking.getManagerNotes() != null ? booking.getManagerNotes() + " | " : "") +
                "Pending charges settled in full ($" + String.format("%.2f", pendingAmount) + "). Order closed.");

        // Release the vehicle
        if (booking.getVehicleId() != null) {
            vehicleRepository.findById(booking.getVehicleId()).ifPresent(v -> {
                if (!"UNDER_MAINTENANCE".equalsIgnoreCase(v.getAvailability())) {
                    v.setAvailability("AVAILABLE");
                    vehicleRepository.save(v);
                }
            });
        }

        return bookingRepository.save(booking);
    }

    /**
     * FR5 Cancel Reservation
     */
    @Transactional
    public Booking cancelBooking(Integer bookingId, String reason) {
        Booking booking = getBookingById(bookingId);
        if ("COMPLETED".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalStateException("Cannot cancel a completed booking.");
        }

        booking.setBookingStatus("CANCELLED");
        booking.setManagerNotes((booking.getManagerNotes() != null ? booking.getManagerNotes() + " | " : "") + 
                "Cancelled: " + (reason != null ? reason : "Customer requested cancellation"));

        // If customer had already paid, process refund status
        if ("PAID".equalsIgnoreCase(booking.getPaymentStatus())) {
            booking.setPaymentStatus("REFUNDED");
            Payment payment = paymentRepository.findFirstByBookingIdOrderByPaymentIdDesc(bookingId).orElse(null);
            if (payment != null) {
                payment.setPaymentStatus("REFUNDED");
                paymentRepository.save(payment);
            }
        }

        // Reset vehicle availability
        Vehicle vehicle = booking.getVehicle();
        if (vehicle != null) {
            vehicle.setAvailability("AVAILABLE");
            vehicleRepository.save(vehicle);
        }

        return bookingRepository.save(booking);
    }
}
