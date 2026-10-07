package com.rental.service;

import com.rental.dto.DamageReportRequest;
import com.rental.dto.VehicleReturnRequest;
import com.rental.entity.Booking;
import com.rental.entity.Vehicle;
import com.rental.repository.BookingRepository;
import com.rental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ReturnService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DamageService damageService;

    @Transactional
    public Booking processVehicleReturn(VehicleReturnRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + request.getBookingId()));

        if ("COMPLETED".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalStateException("This vehicle has already been returned and the booking is marked COMPLETED.");
        }
        if ("PENDING_FINAL_PAYMENT".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new IllegalStateException("This booking is awaiting final payment before it can be closed.");
        }

        LocalDate returnDate = request.getReturnDate() != null ? request.getReturnDate() : LocalDate.now();
        booking.setActualReturnDate(returnDate);
        booking.setReturnOdometer(request.getOdometerReading());
        booking.setReturnFuelLevel(request.getFuelLevel());

        // FR8: Calculate late return charges
        double lateFee = 0.0;
        if (returnDate.isAfter(booking.getRentalEndDate())) {
            long lateDays = ChronoUnit.DAYS.between(booking.getRentalEndDate(), returnDate);
            double baseRate = booking.getBaseDailyRate() != null ? booking.getBaseDailyRate() : 50.0;
            // Daily rate + $30 late penalty per day
            lateFee = lateDays * (baseRate + 30.0);
            booking.setLateFee(lateFee);
            booking.setTotalCharge(Math.round((booking.getTotalCharge() + lateFee) * 100.0) / 100.0);
        }

        // Fuel penalty if returned Low
        double fuelPenalty = 0.0;
        if ("Low".equalsIgnoreCase(request.getFuelLevel())) {
            fuelPenalty = 45.0;
            booking.setTotalCharge(booking.getTotalCharge() + fuelPenalty);
            booking.setManagerNotes((booking.getManagerNotes() != null ? booking.getManagerNotes() + " | " : "") +
                    "Low fuel penalty +$45.00");
        }

        // FR9: Damage Inspection check
        boolean hasDamage = request.isDamageFound();
        double damageCost = 0.0;
        if (hasDamage && request.getEstimatedRepairCost() != null && request.getEstimatedRepairCost() > 0) {
            damageCost = request.getEstimatedRepairCost();
            DamageReportRequest dmgReq = new DamageReportRequest();
            dmgReq.setBookingId(booking.getBookingId());
            dmgReq.setVehicleId(booking.getVehicleId());
            dmgReq.setDamageDescription(request.getDamageDescription() != null ? request.getDamageDescription() : "Return damage reported");
            dmgReq.setRepairCost(damageCost);
            dmgReq.setInspectorName(request.getInspectorNotes() != null ? request.getInspectorNotes() : "Rental Manager");
            damageService.recordDamage(dmgReq);

            // Set damage fee on booking
            booking.setDamageFee(damageCost);
            booking.setTotalCharge(Math.round((booking.getTotalCharge() + damageCost) * 100.0) / 100.0);
        }

        // Determine if there are any outstanding charges that must be paid before closing
        boolean hasPendingCharges = (damageCost > 0) || (lateFee > 0) || (fuelPenalty > 0);

        if (hasPendingCharges) {
            // Cannot close yet — customer must pay pending damage/late/fuel fees first
            booking.setBookingStatus("PENDING_FINAL_PAYMENT");
            booking.setManagerNotes((booking.getManagerNotes() != null ? booking.getManagerNotes() + " | " : "") +
                    String.format("Return processed. Pending payment: damage=$%.2f, late=$%.2f, fuel=$%.2f. Must be paid to close order.",
                            damageCost, lateFee, fuelPenalty));
        } else {
            // No outstanding charges — close immediately
            booking.setBookingStatus("COMPLETED");
            booking = bookingRepository.save(booking);

            // Release the vehicle
            Vehicle vehicle = null;
            if (booking.getVehicleId() != null) {
                vehicle = vehicleRepository.findById(booking.getVehicleId()).orElse(null);
            }
            if (vehicle != null) {
                if (!hasDamage || request.getEstimatedRepairCost() == null || request.getEstimatedRepairCost() <= 200.0) {
                    vehicle.setAvailability("AVAILABLE");
                    vehicleRepository.save(vehicle);
                    booking.setVehicle(vehicle);
                }
            }

            return booking;
        }

        booking = bookingRepository.save(booking);

        // If damage > $200, vehicle goes to maintenance (set by DamageService already)
        // Otherwise keep as RENTED until final payment completes
        return booking;
    }
}
