package com.rental.service;

import com.rental.dto.DamageReportRequest;
import com.rental.entity.Booking;
import com.rental.entity.DamageReport;
import com.rental.entity.Vehicle;
import com.rental.repository.BookingRepository;
import com.rental.repository.DamageReportRepository;
import com.rental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DamageService {

    @Autowired
    private DamageReportRepository damageReportRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<DamageReport> getAllDamageReports() {
        return damageReportRepository.findAllByOrderByDamageIdDesc();
    }

    public List<DamageReport> getDamageReportsByBooking(Integer bookingId) {
        return damageReportRepository.findByBookingId(bookingId);
    }

    @Transactional
    public DamageReport recordDamage(DamageReportRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + request.getBookingId()));

        Integer vehicleId = request.getVehicleId() != null ? request.getVehicleId() : booking.getVehicleId();

        DamageReport report = new DamageReport(
                booking.getBookingId(),
                vehicleId,
                request.getDamageDescription().trim(),
                request.getRepairCost(),
                request.getInspectorName() != null ? request.getInspectorName() : "Rental Manager"
        );
        report = damageReportRepository.save(report);

        // FR9: Add repair charges to final bill
        double currentDamage = booking.getDamageFee() != null ? booking.getDamageFee() : 0.0;
        booking.setDamageFee(currentDamage + request.getRepairCost());
        booking.setTotalCharge(Math.round((booking.getTotalCharge() + request.getRepairCost()) * 100.0) / 100.0);
        bookingRepository.save(booking);

        // Set vehicle to UNDER_MAINTENANCE if heavy repair is required (> $200)
        if (request.getRepairCost() > 200.0) {
            Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
            if (vehicle != null) {
                vehicle.setAvailability("UNDER_MAINTENANCE");
                vehicleRepository.save(vehicle);
            }
        }

        return report;
    }
}
